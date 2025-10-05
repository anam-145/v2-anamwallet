# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ============================================
# Hub API 및 Retrofit 관련 ProGuard 규칙
# ============================================

# 제네릭 메타데이터 완벽 보존
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepattributes *Annotation*

# Kotlin 메타데이터 보존 (중요!)
-keep class kotlin.Metadata { *; }

# Retrofit 인터페이스 (모든 모듈)
-keep interface com.anam145.wallet.**.remote.api.** { *; }
-keepclassmembers interface com.anam145.wallet.**.remote.api.** {
    @retrofit2.http.* <methods>;
}

# Hub API 모델 클래스
-keep class com.anam145.wallet.feature.hub.remote.model.** { *; }
-keep class com.anam145.wallet.feature.hub.remote.model.ApiResponse { *; }
-keep class * extends com.anam145.wallet.feature.hub.remote.model.ApiResponse

# Retrofit 라이브러리
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-dontwarn javax.annotation.**

# Gson 라이브러리
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Keep generic type information for Retrofit
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# OkHttp/OkIO
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase