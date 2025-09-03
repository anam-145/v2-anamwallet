package com.anam145.wallet.core.security.data.repository

import android.content.SharedPreferences
import android.util.Base64
import com.anam145.wallet.core.security.domain.repository.SecurityRepository
import com.anam145.wallet.core.security.model.ScryptParams
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

/**
 * 암호화된 SecurityRepository 구현체
 * 
 * EncryptedSharedPreferences를 사용하여 모든 데이터를 자동으로 암호화합니다.
 * Android Keystore에 저장된 마스터 키로 AES-256 암호화가 적용됩니다.
 */
class EncryptedSecurityRepositoryImpl @Inject constructor(
    @Named("encrypted_security") private val encryptedPrefs: SharedPreferences,
    private val gson: Gson
) : SecurityRepository {
    
    companion object {
        private const val PASSWORD_VERIFIER_KEY = "app_password_verifier"
        private const val PASSWORD_SALT_KEY = "app_password_salt"
        private const val SCRYPT_PARAMS_KEY = "scrypt_params"
    }
    
    /**
     * 비밀번호 파생키 저장
     * 
     * SCrypt KDF로 생성된 파생키를 암호화하여 저장합니다.
     * EncryptedSharedPreferences가 Android Keystore를 통해 AES-256-GCM 암호화를 자동 적용합니다.
     * 
     * 보안 개선:
     * - Root 접근 시에도 암호화된 상태로 보호
     * - Android Keystore의 하드웨어 보안 모듈 활용
     */
    override suspend fun savePasswordVerifier(
        passwordVerifier: ByteArray,
        salt: ByteArray,
        scryptParams: ScryptParams
    ): Result<Unit> = runCatching {
        val encodedVerifier = Base64.encodeToString(passwordVerifier, Base64.NO_WRAP)
        val encodedSalt = Base64.encodeToString(salt, Base64.NO_WRAP)
        val paramsJson = gson.toJson(scryptParams)
        
        // EncryptedSharedPreferences가 자동으로 암호화하여 저장
        encryptedPrefs.edit()
            .putString(PASSWORD_VERIFIER_KEY, encodedVerifier)
            .putString(PASSWORD_SALT_KEY, encodedSalt)
            .putString(SCRYPT_PARAMS_KEY, paramsJson)
            .apply()
    }
    
    /**
     * 비밀번호 파생키 읽기
     * 
     * EncryptedSharedPreferences가 자동으로 복호화하여 반환
     */
    override suspend fun getPasswordVerifier(): ByteArray? {
        val verifierString = encryptedPrefs.getString(PASSWORD_VERIFIER_KEY, null) ?: return null
        return Base64.decode(verifierString, Base64.NO_WRAP)
    }
    
    /**
     * Salt 읽기
     * 
     * 자동으로 복호화된 Salt 반환
     */
    override suspend fun getSalt(): ByteArray? {
        val saltString = encryptedPrefs.getString(PASSWORD_SALT_KEY, null) ?: return null
        return Base64.decode(saltString, Base64.NO_WRAP)
    }
    
    /**
     * SCrypt 파라미터 읽기
     * 
     * 자동으로 복호화된 파라미터 반환
     */
    override suspend fun getScryptParams(): ScryptParams? {
        val paramsString = encryptedPrefs.getString(SCRYPT_PARAMS_KEY, null) ?: return null
        return gson.fromJson(paramsString, ScryptParams::class.java)
    }
    
    /**
     * 비밀번호 존재 여부 확인
     */
    override fun hasPassword(): Flow<Boolean> = flow {
        emit(encryptedPrefs.contains(PASSWORD_VERIFIER_KEY))
    }
    
    /**
     * 모든 데이터 삭제
     */
    override suspend fun clearAll(): Result<Unit> = runCatching {
        encryptedPrefs.edit()
            .remove(PASSWORD_VERIFIER_KEY)
            .remove(PASSWORD_SALT_KEY)
            .remove(SCRYPT_PARAMS_KEY)
            .apply()
    }
}