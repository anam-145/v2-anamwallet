package com.anam145.wallet.feature.identity.ui.digitalid

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anam145.wallet.core.ui.language.LocalStrings
import com.anam145.wallet.feature.identity.R
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigitalIDScreen(
    onBackClick: () -> Unit = {},
    onQRCodeClick: () -> Unit = {}
) {
    val strings = LocalStrings.current
    val scrollState = rememberScrollState()
    
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FBFF),
                        Color(0xFFE8F4FD)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Main ID Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Box {
                        // Background Pattern
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(520.dp)
                                .alpha(0.03f)
                        ) {
                            drawBackgroundPattern(rotationAngle)
                        }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Header Section inside card
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "UNDP Event Check-in",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Scan QR code to verify",
                                            fontSize = 13.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                    
                                    // Active Status Badge
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF10B981),
                                        shadowElevation = 2.dp
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                                    .alpha(pulseAlpha)
                                            )
                                            Text(
                                                text = "Active",
                                                fontSize = 11.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Profile Icon
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFE5E7EB),
                                shadowElevation = 4.dp
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(60.dp),
                                        tint = Color(0xFF6B7280)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Name
                            Text(
                                text = "Jean Uwimana",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            // Role
                            Text(
                                text = "UNDP Program Participant",
                                fontSize = 14.sp,
                                color = Color(0xFF64748B)
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Validity Info Card
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8FBFF)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Issue Date",
                                                fontSize = 11.sp,
                                                color = Color(0xFF94A3B8),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = "2025-01-05",
                                                fontSize = 13.sp,
                                                color = Color(0xFF475569),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        
                                        Box(
                                            modifier = Modifier
                                                .width(1.dp)
                                                .height(30.dp)
                                                .background(Color(0xFFE2E8F0))
                                        )
                                        
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Valid Until",
                                                fontSize = 11.sp,
                                                color = Color(0xFF94A3B8),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                text = "2026-01-04",
                                                fontSize = 13.sp,
                                                color = Color(0xFF475569),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // QR Code Section with glow effect (Clickable)
                            Box(
                                modifier = Modifier.clickable { onQRCodeClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                // Glow effect
                                Box(
                                    modifier = Modifier
                                        .size(140.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    Color(0xFF4DA2FF).copy(alpha = 0.2f),
                                                    Color.Transparent
                                                )
                                            )
                                        )
                                )
                                
                                Surface(
                                    modifier = Modifier.size(120.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    shadowElevation = 4.dp,
                                    border = BorderStroke(1.dp, Color(0xFFE1E8ED))
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // QR Code Image
                                        Image(
                                            painter = painterResource(id = R.drawable.qr_code),
                                            contentDescription = "QR Code - Tap to verify",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Verification Badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFEBF8FF)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color(0xFF0284C7)
                                    )
                                    Text(
                                        text = "Cryptographically Verified",
                                        fontSize = 12.sp,
                                        color = Color(0xFF0284C7),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // DID Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "did:anam145:participant:2NEpo7TZRRrLZSi2U",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF0F172A),
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF4DA2FF)
                        )
                        Text(
                            text = "Secured by ANAM145 Blockchain",
                            fontSize = 10.sp,
                            color = Color(0xFF4DA2FF),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Helper function to draw background pattern
private fun DrawScope.drawBackgroundPattern(rotation: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    
    rotate(degrees = rotation, pivot = Offset(centerX, centerY)) {
        // Draw circular patterns
        for (i in 0..5) {
            val radius = 50f + i * 80f
            drawCircle(
                color = Color(0xFF4DA2FF),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.dp.toPx())
            )
        }
        
        // Draw radial lines
        for (angle in 0..360 step 30) {
            val endX = centerX + 400f * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val endY = centerY + 400f * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
            
            drawLine(
                color = Color(0xFF4DA2FF),
                start = Offset(centerX, centerY),
                end = Offset(endX, endY),
                strokeWidth = 0.5.dp.toPx()
            )
        }
    }
}