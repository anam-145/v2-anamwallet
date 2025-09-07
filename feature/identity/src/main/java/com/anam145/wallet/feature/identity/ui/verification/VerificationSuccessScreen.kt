package com.anam145.wallet.feature.identity.ui.verification

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anam145.wallet.core.ui.language.LocalStrings
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationSuccessScreen(
    onBackClick: () -> Unit = {}
) {
    val strings = LocalStrings.current
    val scrollState = rememberScrollState()
    
    // Success animation
    var isAnimating by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating) 360f else 0f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "rotation"
    )
    
    // Pulse animation for success badge
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    LaunchedEffect(Unit) {
        delay(100)
        isAnimating = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF0FEFF),
                        Color(0xFFE6F7FF)
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
            
            // Success Animation Circle (Smaller)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // Animated circles background
                Canvas(
                    modifier = Modifier
                        .size(180.dp)
                        .alpha(0.3f)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    
                    // Outer circle
                    drawCircle(
                        color = Color(0xFF10B981),
                        radius = size.width / 2 * pulseScale,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                    
                    // Middle circle
                    drawCircle(
                        color = Color(0xFF34D399),
                        radius = size.width / 3 * pulseScale,
                        center = center,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    
                    // Inner circle
                    drawCircle(
                        color = Color(0xFF6EE7B7),
                        radius = size.width / 6 * pulseScale,
                        center = center,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                
                // Success Icon (Smaller)
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale)
                        .rotate(rotation),
                    shape = CircleShape,
                    color = Color(0xFF10B981),
                    shadowElevation = 8.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            modifier = Modifier.size(70.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Success Message (Compact)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Identity Verified!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Successfully verified",
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Verification Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "UNDP Event Check-in",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Verification Complete",
                                fontSize = 14.sp,
                                color = Color(0xFF10B981)
                            )
                        }
                        
                        // Verified Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF10B981)
                                )
                                Text(
                                    text = "Verified",
                                    fontSize = 11.sp,
                                    color = Color(0xFF10B981),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Verification Details (Simplified)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DetailRow(
                            label = "Verification Time",
                            value = "2025-08-27 08:58 AM"
                        )
                        DetailRow(
                            label = "Verification Method",
                            value = "QR Code Scan"
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Blockchain Info
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF0F9FF)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF0284C7)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column {
                                Text(
                                    text = "Blockchain Transaction",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF0284C7)
                                )
                                Text(
                                    text = "0x7f3a...9b2c",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Button
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4DA2FF)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Done",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color(0xFF94A3B8)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569)
        )
    }
}