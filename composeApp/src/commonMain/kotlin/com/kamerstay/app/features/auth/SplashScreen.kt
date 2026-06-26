package com.kamerstay.app.features.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.core.utils.APP_NAME
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(5000)
        navController.navigate(Routes.Onboarding.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    // Animation du dot actif
    val dotAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dot"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OnSecondary),
        contentAlignment = Alignment.Center
    ) {
        // Points décoratifs background (comme des étoiles)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val dots = listOf(
                Pair(50f, 200f), Pair(620f, 150f), Pair(100f, 800f),
                Pair(680f, 600f), Pair(200f, 1400f), Pair(600f, 1200f),
                Pair(350f, 300f), Pair(80f, 1100f), Pair(650f, 900f),
                Pair(150f, 500f), Pair(700f, 400f), Pair(400f, 1600f),
            )
            dots.forEach { (x, y) ->
                drawCircle(
                    color = Outline,
                    radius = 2f,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Logo Triangles ────────────────────────
            KamerStayLogo()

            Spacer(modifier = Modifier.height(40.dp))

            // ── App Name ──────────────────────────────
            Text(
                text = APP_NAME,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tagline ───────────────────────────────
            Text(
                text = "DIGITAL HOSPITALITY",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceSecondary,
                textAlign = TextAlign.Center,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Dots pagination ───────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                repeat(3) { index ->
                    val isActive = dotAnim.toInt() == index
                    Box(
                        modifier = Modifier
                            .size(if (isActive) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) Primary  // #00D5E1 turquoise
                                else Color.White.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

// ── Logo KamerStay — Double Triangle ─────────────────────
@Composable
fun KamerStayLogo() {
    Canvas(modifier = Modifier.size(120.dp)) {
        val w = size.width
        val h = size.height

        val backTriangle = Path().apply {
            moveTo(w * 0.35f, h * 0.85f)
            lineTo(w * 0.72f, h * 0.28f)
            lineTo(w * 0.98f, h * 0.85f)
            close()
        }

        val frontTriangle = Path().apply {
            moveTo(w * 0.05f, h * 0.85f)
            lineTo(w * 0.42f, h * 0.22f)
            lineTo(w * 0.75f, h * 0.85f)
            close()
        }

        val overlapTriangle = Path().apply {
            moveTo(w * 0.35f, h * 0.85f)
            lineTo(w * 0.42f, h * 0.22f)
            lineTo(w * 0.55f, h * 0.85f)
            close()
        }

        drawPath(
            path = backTriangle,
            color = TriangleBack
        )
        drawPath(
            path = frontTriangle,
            color = TriangleFront
        )
        drawPath(
            path = overlapTriangle,
            color = TriangleShadow.copy(alpha = 0.6f)
        )
    }
}