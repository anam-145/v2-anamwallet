package com.anam145.wallet.feature.miniapp.blockchain.ui

import android.webkit.WebView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anam145.wallet.core.ui.components.Header
import com.anam145.wallet.core.ui.language.LocalStrings
import com.anam145.wallet.core.ui.language.getStringsForSkinAndLanguage
import com.anam145.wallet.core.common.model.Skin
import com.anam145.wallet.core.common.model.Language
import com.anam145.wallet.feature.miniapp.common.data.common.MiniAppFileManager
import androidx.compose.runtime.CompositionLocalProvider
import com.anam145.wallet.feature.miniapp.blockchain.ui.components.BlockchainWebView
import com.anam145.wallet.feature.miniapp.common.ui.components.ErrorContent
import com.anam145.wallet.feature.miniapp.common.ui.components.ServiceConnectionCard
import com.anam145.wallet.feature.miniapp.common.ui.components.ResponsiveWebViewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockchainScreen(
    blockchainId: String,
    viewModel: BlockchainViewModel,
    fileManager: MiniAppFileManager,
    skin: Skin = Skin.ANAM,
    language: Language = Language.KOREAN
) {
    // 스킨과 언어에 맞는 문자열 가져오기
    val strings = getStringsForSkinAndLanguage(skin, language)
    
    // CompositionLocal로 문자열 제공
    CompositionLocalProvider(
        LocalStrings provides strings
    ) {
        BlockchainScreenContent(
            blockchainId = blockchainId,
            viewModel = viewModel,
            fileManager = fileManager
        )
    }
}

@Composable
private fun BlockchainScreenContent(
    blockchainId: String,
    viewModel: BlockchainViewModel,
    fileManager: MiniAppFileManager
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var webView by remember { mutableStateOf<WebView?>(null) }
    val strings = LocalStrings.current
    
    // 초기화
    LaunchedEffect(key1 = blockchainId) {
        viewModel.initialize(blockchainId)
    }
    
    // URL 로드 처리
    LaunchedEffect(key1 = uiState.webUrl) {
        uiState.webUrl?.let { url ->
            webView?.loadUrl(url)
        }
    }
    
    Scaffold(
        topBar = {
            Header(
                title = strings.headerTitle,
                showBackButton = false,  // 뒤로가기 버튼 제거
                onTitleClick = {  // 타이틀 클릭 시 뒤로가기
                    viewModel.handleIntent(BlockchainContract.Intent.NavigateBack)
                },
                showBlockchainStatus = uiState.isActivated,
                activeBlockchainName = if (uiState.isActivated) {
                    uiState.manifest?.name ?: "Activated"
                } else null
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    ErrorContent(
                        error = uiState.error ?: "오류가 발생했습니다",
                        onRetry = {
                            viewModel.handleIntent(BlockchainContract.Intent.DismissError)
                            viewModel.initialize(blockchainId)
                        }
                    )
                }
                uiState.manifest != null -> {
                    uiState.manifest?.let { manifest ->
                        ResponsiveWebViewContainer {
                            BlockchainWebView(
                                blockchainId = blockchainId,
                                manifest = manifest,
                                fileManager = fileManager,
                                onWebViewCreated = { 
                                    webView = it
                                    viewModel.onWebViewReady()
                                }
                            )
                        }
                    }
                }
            }
            
            // 서비스 연결 상태 표시 (10초 타임아웃 후에만 표시)
            if (!uiState.isServiceConnected && uiState.connectionTimeout) {
                ServiceConnectionCard(
                    onRetry = {
                        viewModel.handleIntent(BlockchainContract.Intent.RetryServiceConnection)
                    }
                )
            }
        }
    }
}