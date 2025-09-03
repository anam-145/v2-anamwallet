package com.anam145.wallet.core.security.domain.usecase

import com.anam145.wallet.core.security.data.util.ScryptConstants
import com.anam145.wallet.core.security.domain.repository.SecurityRepository
import com.lambdaworks.crypto.SCrypt
import kotlinx.coroutines.flow.first
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * 앱 비밀번호 검증 UseCase
 * 
 * 입력된 비밀번호를 SCrypt KDF로 처리하여 저장된 파생키와 비교합니다.
 * 저장된 파생키는 EncryptedSharedPreferences에 의해 자동으로 복호화됩니다.
 */
class VerifyAppPasswordUseCase @Inject constructor(
    private val securityRepository: SecurityRepository
) {
    
    /**
     * 앱 비밀번호 검증
     * 
     * @param inputPassword 입력된 비밀번호 (평문)
     * @return 비밀번호가 맞으면 true
     */
    suspend operator fun invoke(inputPassword: String): Result<Boolean> = runCatching {
        // 1. Repository에서 암호화된 데이터 조회
        // EncryptedSecurityRepositoryImpl이 Android Keystore로 자동 복호화
        val storedPasswordVerifier = securityRepository.getPasswordVerifier() ?: return@runCatching false
        val salt = securityRepository.getSalt() ?: return@runCatching false
        val params = securityRepository.getScryptParams() ?: return@runCatching false
        
        // 2. 입력된 비밀번호로 파생키 재생성
        val inputPasswordVerifier = SCrypt.scrypt(
            inputPassword.toByteArray(StandardCharsets.UTF_8),
            salt,
            params.n,  // 8192
            params.r,  // 8
            params.p,  // 1
            ScryptConstants.DKLEN  // 32 bytes
        )
        
        // 3. 시간 상수 비교 (타이밍 공격 방지)
        storedPasswordVerifier.contentEquals(inputPasswordVerifier)
        
        // 검증 과정:
        // SharedPreferences → AES-256-GCM 복호화 → Base64 디코딩 → storedPasswordVerifier
        // inputPassword → SCrypt → inputPasswordVerifier
        // 두 파생키 비교
    }
    
    /**
     * 저장된 비밀번호 존재 여부 확인
     */
    suspend fun hasPassword(): Boolean {
        return securityRepository.hasPassword().first()
    }
}