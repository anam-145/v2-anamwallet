package com.anam145.wallet.feature.main.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anam145.wallet.core.common.model.Skin
import com.anam145.wallet.feature.main.R

/**
 * 테마별 일러스트레이션을 표시하는 컴포저블
 * 
 * - SEOUL: 서울 특색 일러스트 (남산타워, 한강 등)
 * - BUSAN: 부산 특색 일러스트 (광안대교, 해운대 등)
 * - LA: LA 특색 일러스트 (헐리우드 사인, 팜트리 등)
 * - ANAM: 일러스트 없음
 */
@Composable
fun ThemeIllustration(
    skin: Skin,
    modifier: Modifier = Modifier
) {
    when (skin) {
        Skin.SEOUL -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_seoul),
                    contentDescription = "Seoul Theme Illustration",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .alpha(0.3f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Skin.BUSAN -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_busan),
                    contentDescription = "Busan Theme Illustration",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .alpha(0.3f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Skin.LA -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_la),
                    contentDescription = "LA Theme Illustration",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .alpha(0.3f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Skin.ANAM -> {
            // ANAM 테마는 일러스트 없음
        }
        Skin.RWANDA -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.illustration_rwanda),
                    contentDescription = "Rwanda Theme Illustration",
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .alpha(0.3f),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}