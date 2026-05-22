package com.kamerstay.app.features.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.DeepEmerald
import com.kamerstay.app.core.theme.OnPrimaryContainer
import com.kamerstay.app.core.theme.WarmAmber
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(5000)
        navController.navigate(Routes.Welcome.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val logoAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepEmerald.copy(alpha = 1.15f), // légèrement plus clair
                        DeepEmerald,                     // couleur principale
                        OnPrimaryContainer,              // plus sombre en bas
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.size(160.dp)
            ) {
                // ── Losange (diamant) ──────────────────────
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomStart)
                        .rotate(45f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF005A4E),
                                    Color(0xFF004D40),
                                )
                            )
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.rotate(-45f)) {
                        MountainIcon()
                    }
                }

                // ── Étoile — touche le coin du losange ────
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = (-25).dp, y = 25.dp)  // ← ajustez ces valeurs
                        .clip(CircleShape)
                        .background(WarmAmber),
                    contentAlignment = Alignment.Center
                ) {
                    StarIcon()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "KamerStay",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = WarmAmber,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "PREMIUM HOSPITALITY",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color.White.copy(alpha = 0.5f),
                    strokeWidth = 2.dp,
                    trackColor = Color.Transparent
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Exploring the heart of Africa...",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f),
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun MountainIcon() {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(64.dp)) {
        val w = size.width
        val h = size.height

        // Montagne gauche (petite)
        val leftPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.02f, h * 0.82f)
            lineTo(w * 0.30f, h * 0.38f)
            lineTo(w * 0.55f, h * 0.82f)
            close()
        }

        // Montagne droite (grande)
        val rightPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(w * 0.28f, h * 0.82f)
            lineTo(w * 0.62f, h * 0.18f)
            lineTo(w * 0.98f, h * 0.82f)
            close()
        }

        drawPath(path = leftPath, color = WarmAmber)
        drawPath(path = rightPath, color = WarmAmber)
    }
}

@Composable
fun StarIcon() {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(22.dp)) {
        val cx = size.width / 2
        val cy = size.height / 2
        val outerR = size.width * 0.45f
        val innerR = size.width * 0.20f

        val starPath = androidx.compose.ui.graphics.Path().apply {
            for (i in 0 until 10) {
                val angle = (PI / 5 * i - PI / 2).toFloat()
                val r = if (i % 2 == 0) outerR else innerR
                val x = cx + r * cos(angle)
                val y = cy + r * sin(angle)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        // FILLED — pas de Stroke
        drawPath(
            path = starPath,
            color = DeepEmerald
        )
    }
}