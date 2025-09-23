package com.anam145.wallet.feature.miniapp.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * 태블릿에서 폰 크기로 WebView 콘텐츠를 제한하는 표준 폰 너비
 * MainActivity의 ResponsiveContentWrapper와 동일한 값 사용
 */
private const val PHONE_MAX_WIDTH_DP = 412

/**
 * 화면 크기가 컴팩트(폰)인지 확인하는 임계값
 * MainActivity의 ResponsiveContentWrapper와 동일한 값 사용
 */
private const val COMPACT_WIDTH_THRESHOLD_DP = 600

/**
 * WebView를 위한 반응형 컨테이너
 * 태블릿에서는 폰 크기로 제한하고, 양옆에 배경색을 표시
 * 
 * @param modifier 수정자
 * @param showBackground 태블릿에서 양옆 배경 표시 여부 (기본: true)
 * @param content WebView 컨텐츠
 */
@Composable
fun ResponsiveWebViewContainer(
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    
    // 화면이 600dp보다 작으면 폰으로 간주
    val isCompact = screenWidthDp < COMPACT_WIDTH_THRESHOLD_DP
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (!isCompact && showBackground) {
                    Modifier.background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = if (isCompact) {
                Modifier.fillMaxSize()
            } else {
                Modifier
                    .widthIn(max = PHONE_MAX_WIDTH_DP.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            }
        ) {
            content()
        }
    }
}