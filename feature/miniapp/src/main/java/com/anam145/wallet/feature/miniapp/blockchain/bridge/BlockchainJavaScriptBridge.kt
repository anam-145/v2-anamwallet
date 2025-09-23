package com.anam145.wallet.feature.miniapp.blockchain.bridge

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.anam145.wallet.feature.miniapp.IKeystoreDecryptCallback
import com.anam145.wallet.feature.miniapp.IMainBridgeService
import com.anam145.wallet.feature.miniapp.common.bridge.service.MainBridgeService
import com.google.gson.Gson

/**
 * 블록체인 서비스용 JavaScript Bridge
 * 
 * BlockchainService의 헤드리스 WebView에서 사용됩니다.
 * 트랜잭션 응답을 처리하는 역할을 합니다.
 */
class BlockchainJavaScriptBridge(
    private val onResponse: (String, String) -> Unit,
    private val context: Context? = null,
    private val webView: WebView? = null
) {
    companion object {
        private const val TAG = "BlockchainService"
    }
    
    private val handler = Handler(Looper.getMainLooper())
    private var mainBridgeService: IMainBridgeService? = null
    private val gson = Gson()
    private var isServiceBound = false
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mainBridgeService = IMainBridgeService.Stub.asInterface(service)
            Log.d(TAG, "Connected to MainBridgeService for Keystore access")
        }
        
        override fun onServiceDisconnected(name: ComponentName?) {
            mainBridgeService = null
            Log.d(TAG, "Disconnected from MainBridgeService")
        }
    }
    
    init {
        bindToMainBridgeService()
    }
    
    private fun bindToMainBridgeService() {
        if (context == null) {
            Log.e(TAG, "Context is null, cannot bind to MainBridgeService")
            return
        }
        
        try {
            val intent = Intent(context, MainBridgeService::class.java)
            val bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            if (!bound) {
                Log.e(TAG, "Failed to bind to MainBridgeService")
            } else {
                Log.d(TAG, "Binding to MainBridgeService initiated")
                isServiceBound = true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding to MainBridgeService", e)
        }
    }
    
    fun cleanup() {
        if (isServiceBound && context != null) {
            try {
                context.unbindService(serviceConnection)
                isServiceBound = false
            } catch (e: Exception) {
                Log.e(TAG, "Error unbinding service", e)
            }
        }
    }
    
    @JavascriptInterface
    fun sendTransactionResponse(requestId: String, responseJson: String) {
        Log.d(TAG, "sendTransactionResponse from blockchain: $requestId")
        handler.post {
            onResponse(requestId, responseJson)
        }
    }
    
    /**
     * Universal Bridge 응답 전송
     * 
     * 블록체인 미니앱에서 Universal Bridge 요청에 대한 응답을 전송합니다.
     */
    @JavascriptInterface
    fun sendUniversalResponse(requestId: String, responseJson: String) {
        Log.d(TAG, "sendUniversalResponse from blockchain: $requestId")
        handler.post {
            onResponse(requestId, responseJson)
        }
    }
    
    @JavascriptInterface
    fun log(message: String) {
        Log.d(TAG, "Blockchain JS: $message")
    }
    
    /**
     * Keystore 복호화 (읽기 전용)
     * 헤드리스 WebView에서 트랜잭션 서명을 위해 private key 접근이 필요한 경우 사용
     */
    @JavascriptInterface
    fun decryptKeystore(keystoreJson: String) {
        Log.d(TAG, "========== Headless decryptKeystore START ==========")
        Log.d(TAG, "[Headless] Keystore length: ${keystoreJson.length}")
        Log.d(TAG, "[Headless] First 100 chars: ${keystoreJson.take(100)}...")
        
        if (keystoreJson.isBlank()) {
            Log.e(TAG, "[Headless] ERROR: Keystore JSON is blank")
            sendDecryptError("Keystore JSON is required")
            return
        }
        
        val service = mainBridgeService
        if (service == null) {
            Log.e(TAG, "[Headless] ERROR: MainBridgeService not connected")
            sendDecryptError("Service not connected")
            return
        }
        
        Log.d(TAG, "[Headless] Calling MainBridgeService.decryptKeystore...")
        try {
            service.decryptKeystore(keystoreJson, object : IKeystoreDecryptCallback.Stub() {
                override fun onSuccess(address: String, secret: String) {
                    Log.d(TAG, "[Headless] ✅ Decryption SUCCESS")
                    Log.d(TAG, "[Headless] Address: $address")
                    Log.d(TAG, "[Headless] Data length: ${secret.length}")
                    Log.d(TAG, "[Headless] First 40 chars: ${secret.take(40)}...")
                    Log.d(TAG, "[Headless] Has 0x prefix: ${secret.startsWith("0x")}")
                    
                    // Hex 디코딩 테스트
                    try {
                        val testBytes = secret.chunked(2).take(20).map { it.toInt(16).toByte() }
                        val testString = String(testBytes.toByteArray())
                        Log.d(TAG, "[Headless] Decoded preview: $testString")
                    } catch (e: Exception) {
                        Log.e(TAG, "[Headless] Failed to decode preview: ${e.message}")
                    }
                    
                    sendDecryptResult(true, address, secret, null)
                }
                
                override fun onError(errorMessage: String) {
                    Log.e(TAG, "[Headless] ❌ Decryption FAILED: $errorMessage")
                    sendDecryptResult(false, null, null, errorMessage)
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "[Headless] Exception in decryptKeystore", e)
            sendDecryptError(e.message ?: "Unknown error")
        }
        Log.d(TAG, "========== Headless decryptKeystore END ==========")
    }
    
    private fun sendDecryptResult(success: Boolean, address: String?, secret: String?, error: String?) {
        handler.post {
            val script = if (success) {
                val addressJson = gson.toJson(address)
                // secret - Native에서 받은 그대로 전달 (mnemonic hex 또는 wallet JSON hex)
                val secretJson = gson.toJson(secret)
                """
                window.dispatchEvent(new CustomEvent('keystoreDecrypted', {
                    detail: {
                        success: true,
                        address: $addressJson,
                        secret: $secretJson
                    }
                }));
                """.trimIndent()
            } else {
                val errorJson = gson.toJson(error)
                """
                window.dispatchEvent(new CustomEvent('keystoreDecrypted', {
                    detail: {
                        success: false,
                        error: $errorJson
                    }
                }));
                """.trimIndent()
            }
            
            webView?.evaluateJavascript(script, null)
        }
    }
    
    private fun sendDecryptError(error: String) {
        sendDecryptResult(false, null, null, error)
    }
}