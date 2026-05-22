package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.KamerStayButton
import com.kamerstay.app.core.components.KamerStayOutlinedButton
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*


@Composable
fun WelcomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Hero Image ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Background simulé (remplacer par vraie photo plus tard)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1A3A4A),
                                    Color(0xFF0D2535),
                                    Color(0xFF1C3D2E),
                                )
                            )
                        )
                )

                // Overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.3f)
                                )
                            )
                        )
                )

                // ── Top Bar Logo ──────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Hotel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "KamerStay",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // ── White Card ────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-32).dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column {
                    // ── Badge Vérifié ─────────────────────
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = null,
                            tint = ErrorColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "VERIFIED LISTINGS ONLY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ErrorColor,
                            letterSpacing = 1.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Titre Bicolore ────────────────────
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = OnSurface)) {
                                append("Experience\n")
                            }
                            withStyle(SpanStyle(color = WarmAmber)) {
                                append("Cameroonian\n")
                            }
                            withStyle(SpanStyle(color = OnSurface)) {
                                append("Hospitality")
                            }
                        },
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 38.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Description ───────────────────────
                    Text(
                        text = "Book verified hotels near your favorite landmarks with ease. From the coast of Douala to the hills of Yaoundé.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = OnSurfaceVariant,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Boutons ───────────────────────────
                    KamerStayButton(
                        text = "Sign Up  →",
                        onClick = { navController.navigate(Routes.SignUp.route) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    KamerStayOutlinedButton(
                        text = "Sign In",
                        onClick = { navController.navigate(Routes.SignIn.route) }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // ── 3 Features ────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WelcomeFeature(
                            icon = Icons.Outlined.Place,
                            label = "LOCAL\nLANDMARKS"
                        )
                        WelcomeFeature(
                            icon = Icons.Outlined.Shield,
                            label = "SECURE\nPAY"
                        )
                        WelcomeFeature(
                            icon = Icons.Outlined.SupportAgent,
                            label = "24/7\nSUPPORT"
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Footer Links ──────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Privacy Policy",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "  •  ",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "Terms of Service",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Copyright ─────────────────────────
                    Text(
                        text = "© 2025 KamerStay. Built with Modern African Hospitality.",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun WelcomeFeature(
    icon: ImageVector,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DeepEmerald,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center,
            letterSpacing = 0.8.sp,
            lineHeight = 13.sp
        )
    }
}


