package com.anam145.wallet.core.security.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.anam145.wallet.core.security.data.repository.EncryptedSecurityRepositoryImpl
import com.anam145.wallet.core.security.data.util.KdfParamsTypeAdapter
import com.anam145.wallet.core.security.domain.repository.SecurityRepository
import com.anam145.wallet.core.security.model.KdfParams
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Security 모듈 의존성 제공
 * 
 * Android Keystore 기반 암호화를 사용하여 민감한 데이터를 보호합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {
    
    // 암호화된 구현체를 사용하도록 변경
    @Binds
    abstract fun bindSecurityRepository(
        encryptedSecurityRepositoryImpl: EncryptedSecurityRepositoryImpl
    ): SecurityRepository
    
    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return GsonBuilder()
                .registerTypeAdapter(KdfParams::class.java, KdfParamsTypeAdapter())
                .setPrettyPrinting()
                .create()
        }
        
        /**
         * 암호화된 SharedPreferences 제공
         * Android Keystore를 사용하여 자동으로 암호화/복호화
         */
        @Provides
        @Singleton
        @Named("encrypted_security")
        fun provideEncryptedSharedPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences {
            // Master Key 생성 (Android Keystore에 저장됨)
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            // 암호화된 SharedPreferences 생성
            return EncryptedSharedPreferences.create(
                context,
                "encrypted_security_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
        
    }
}