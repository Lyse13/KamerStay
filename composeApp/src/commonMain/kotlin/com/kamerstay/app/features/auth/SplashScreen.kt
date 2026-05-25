package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import kamerstay.composeapp.generated.resources.Res
import androidx.compose.ui.layout.ContentScale
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.DeepEmerald
import com.kamerstay.app.core.theme.OnPrimaryContainer
import com.kamerstay.app.core.theme.WarmAmber
import kamerstay.composeapp.generated.resources.logo_icon
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(Unit) {
        delay(9000)
        navController.navigate(Routes.Welcome.route) {
            popUpTo(Routes.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepEmerald.copy(alpha = 1.15f),
                        DeepEmerald,
                        OnPrimaryContainer,
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
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Image(
                    painter = painterResource(Res.drawable.logo_icon),
                    contentDescription = "KamerStay Logo",
                    modifier = Modifier.size(160.dp),
                    contentScale = ContentScale.Fit
                )
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
                    color = WarmAmber,
                    strokeWidth = 2.dp,
                    trackColor = Color.Transparent
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Exploring the heart of Cameroon...",
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