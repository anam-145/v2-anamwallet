package com.anam145.wallet.core.security.domain.usecase

import com.anam145.wallet.core.security.data.util.ScryptConstants
import com.anam145.wallet.core.security.domain.repository.SecurityRepository
import com.anam145.wallet.core.security.model.ScryptParams
import com.lambdaworks.crypto.SCrypt
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.inject.Inject

/**
 * 앱 비밀번호 저장 UseCase
 * 
 * 사용자의 앱 비밀번호를 SCrypt KDF로 처리하여 안전하게 저장합니다.
 * 저장된 파생키는 EncryptedSharedPreferences를 통해 AES-256-GCM으로 암호화됩니다.
 */
class SaveAppPasswordUseCase @Inject constructor(
    private val securityRepository: SecurityRepository
) {
    
    /**
     * 앱 비밀번호를 파생키로 변환하여 저장
     * 
     * @param password 사용자 비밀번호 (평문)
     * @return 저장 성공 여부
     */
    suspend operator fun invoke(password: String): Result<Unit> = runCatching {
        // 1. Salt 생성 (256비트 랜덤)
        val salt = ByteArray(32)
        SecureRandom().nextBytes(salt)
        
        // 2. SCrypt KDF로 파생키 생성 (해시가 아님!)
        // 비밀번호 → Key Derivation Function → 파생키
        val passwordVerifier = SCrypt.scrypt(
            password.toByteArray(StandardCharsets.UTF_8),
            salt,
            ScryptConstants.N,  // 8192 (2^13)
            ScryptConstants.R,  // 8
            ScryptConstants.P,  // 1
            ScryptConstants.DKLEN  // 32 bytes
        )
        
        // 3. Repository를 통해 암호화 저장
        // EncryptedSecurityRepositoryImpl이 Android Keystore로 자동 암호화
        val scryptParams = ScryptParams(ScryptConstants.N, ScryptConstants.R, ScryptConstants.P)
        
        securityRepository.savePasswordVerifier(
            passwordVerifier = passwordVerifier,  // 기술적으로 정확한 명명!
            salt = salt,
            scryptParams = scryptParams
        ).getOrThrow()
        
        // 저장 과정:
        // passwordVerifier → Base64 인코딩 → AES-256-GCM 암호화 → SharedPreferences
    }
}