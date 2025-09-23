package com.anam145.wallet.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified

/**
 * 태블릿에서 폰 크기로 콘텐츠를 제한하는 표준 폰 너비
 */
const val PHONE_MAX_WIDTH_DP = 412

/**
 * 화면 크기가 컴팩트(폰)인지 확인하는 임계값
 */
const val COMPACT_WIDTH_THRESHOLD_DP = 600

/**
 * 반응형 콘텐츠 래퍼 - 태블릿에서 폰 크기로 콘텐츠를 제한
 * 
 * @param modifier 수정자
 * @param maxWidthDp 태블릿에서 적용할 최대 너비 (기본값: 412dp)
 * @param content 표시할 콘텐츠
 */
@Composable
fun ResponsiveContentWrapper(
    modifier: Modifier = Modifier,
    maxWidthDp: Int = PHONE_MAX_WIDTH_DP,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    
    // 화면이 600dp보다 작으면 폰으로 간주
    val isCompact = screenWidthDp < COMPACT_WIDTH_THRESHOLD_DP
    
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val widthModifier = if (isCompact) {
            Modifier.fillMaxWidth()
        } else {
            Modifier.widthIn(max = maxWidthDp.dp)
        }
        
        Box(
            modifier = widthModifier.fillMaxHeight()
        ) {
            content()
        }
    }
}

/**
 * 커스텀 최대 너비를 지원하는 반응형 래퍼
 * WindowSizeClass를 사용하는 레거시 버전과의 호환성을 위해 유지
 */
@Composable
fun TabletConstrainedLayout(
    maxWidth: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val widthModifier = if (maxWidth.isSpecified) {
            Modifier.widthIn(max = maxWidth)
        } else {
            Modifier.fillMaxWidth()
        }
        
        Box(
            modifier = widthModifier.fillMaxHeight()
        ) {
            content()
        }
    }
}