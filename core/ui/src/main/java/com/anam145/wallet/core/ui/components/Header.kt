package com.anam145.wallet.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anam145.wallet.core.ui.language.LocalStrings
import androidx.compose.ui.graphics.Color

/**
 * ANAM Wallet 공통 헤더 컴포넌트
 * 
 * anam-android와 동일한 디자인:
 * - 높이: 56dp + 상태바 패딩 + 블록체인 칩
 * - 좌우 패딩: 24dp
 * - 타이틀: 24sp, Bold
 * - 블록체인 상태 칩: 타이틀 아래 표시
 * 
 * @param title 표시할 타이틀
 * @param showBackButton 뒤로가기 버튼 표시 여부
 * @param onBackClick 뒤로가기 버튼 클릭 콜백
 * @param onTitleClick 타이틀 클릭 콜백
 * @param showBlockchainStatus 블록체인 상태 표시 여부
 * @param activeBlockchainName 활성 블록체인 이름
 * @param onBlockchainClick 블록체인 칩 클릭 콜백
 */
@Composable
fun Header(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onTitleClick: (() -> Unit)? = null,
    showBlockchainStatus: Boolean = false,
    activeBlockchainName: String? = null,
    onBlockchainClick: (() -> Unit)? = null
) {
    // 헤더 배경색 고정 (테마와 무관)
    val fixedBackgroundColor = Color(0xFFFCFCFC)
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = fixedBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp)
        ) {
            // 타이틀과 뒤로가기 버튼
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // 뒤로가기 버튼
                if (showBackButton && onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                
                // 타이틀 (고정 색상, 클릭 가능)
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F),  // 고정된 텍스트 색상
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = if (showBackButton) 48.dp else 0.dp)
                        .then(
                            if (onTitleClick != null) {
                                Modifier
                                    .clickable { 
                                        onTitleClick() 
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)  // 터치 영역 확대
                            } else {
                                Modifier
                            }
                        )
                )
            }
            
            // 블록체인 상태 표시 (옵션)
            AnimatedVisibility(
                visible = showBlockchainStatus && !activeBlockchainName.isNullOrEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp, bottom = 12.dp)  // 상단은 작게, 하단은 적당히
                ) {
                    BlockchainStatusChip(
                        blockchainName = activeBlockchainName ?: "",
                        onClick = onBlockchainClick
                    )
                }
            }
        }
    }
}

/**
 * 블록체인 활성화 상태를 표시하는 칩
 * 
 * @param blockchainName 활성 블록체인 이름
 * @param onClick 클릭 콜백
 */
@Composable
private fun BlockchainStatusChip(
    blockchainName: String,
    onClick: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .height(32.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White  // 흰색 배경
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE4E4E7)  // 연한 회색 보더
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔗 $blockchainName Activated",
                fontSize = 12.sp,
                color = Color(0xFF3F3F46)  // 고정된 다크 그레이 텍스트
            )
        }
    }
}