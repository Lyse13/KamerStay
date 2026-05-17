package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*


@Composable
fun SignInScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // ── Top Bar ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DeepEmerald),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Hotel,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "KamerStay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }

                // Close button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Card principale ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
            ) {
                Column {
                    // ── Hero Image simulée ────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 24.dp
                            ))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2D5016),
                                        Color(0xFF1C3D2E),
                                        DeepEmerald,
                                    )
                                )
                            )
                    ) {
                        // Décoration abstraite représentant une chambre d'hôtel
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF3D6B20).copy(alpha = 0.6f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                        Text(
                            text = "🌿  🛋️  🪴",
                            fontSize = 40.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    // ── Contenu du form ───────────────────
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 28.dp
                        )
                    ) {
                        // Titre
                        Text(
                            text = "Welcome Back",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OnSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Sign in to manage your bookings and explore new destinations across Cameroon.",
                            fontSize = 14.sp,
                            color = OnSurfaceVariant,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // ── Email ──────────────────────────
                        Text(
                            text = "EMAIL ADDRESS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = {
                                Text(
                                    "name@example.com",
                                    color = OnSurfaceVariant.copy(0.5f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = authTextFieldColors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // ── Password Label + Forgot ────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PASSWORD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Forgot Password?",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = WarmAmber,
                                modifier = Modifier.clickable {
                                    navController.navigate(Routes.ForgotPassword.route) // ← ajouter ceci
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // ── Password Field ─────────────────
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = {
                                Text(
                                    "••••••••",
                                    color = OnSurfaceVariant.copy(0.5f)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { passwordVisible = !passwordVisible }
                                ) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Outlined.VisibilityOff
                                        else Icons.Outlined.Visibility,
                                        contentDescription = null,
                                        tint = OnSurfaceVariant
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = authTextFieldColors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(28.dp))

                        // ── Sign In Button ─────────────────
                        Button(
                            onClick = {
                                navController.navigate(Routes.TravelerHome.route) {
                                popUpTo(Routes.Welcome.route) { inclusive = true }
                            } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepEmerald
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Sign In  →",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )

                            }
                        }

                        TextButton(
                            onClick = {
                                navController.navigate(Routes.ManagerDashboard.route) {
                                    popUpTo(Routes.Welcome.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Login as Manager (Test)",
                                color = OnSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // ── Divider OR CONTINUE WITH ───────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Divider
                            )
                            Text(
                                text = "  OR CONTINUE WITH  ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.8.sp
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Divider
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ── Social Buttons ─────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Google
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder(true).copy(
                                    width = 1.dp
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = OnSurface
                                )
                            ) {
                                Text(text = "G", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Google",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnSurface
                                )
                            }

                            // Apple
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder(true).copy(
                                    width = 1.dp
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = OnSurface
                                )
                            ) {
                                Text(text = "i05", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Apple",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = OnSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // ── Sign Up Link ───────────────────
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = OnSurfaceVariant)) {
                                    append("New here? ")
                                }
                                withStyle(SpanStyle(
                                    color = OnSurface,
                                    fontWeight = FontWeight.Bold
                                )) {
                                    append("Sign Up")
                                }
                            },
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Routes.SignUp.route)
                                },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Footer badges ─────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = OnSurfaceVariant.copy(0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VERIFIED PROPERTIES",
                        fontSize = 10.sp,
                        color = OnSurfaceVariant.copy(0.5f),
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = null,
                        tint = OnSurfaceVariant.copy(0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SECURE PAYMENTS",
                        fontSize = 10.sp,
                        color = OnSurfaceVariant.copy(0.5f),
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.SupportAgent,
                        contentDescription = null,
                        tint = OnSurfaceVariant.copy(0.5f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "LOCAL SUPPORT",
                        fontSize = 10.sp,
                        color = OnSurfaceVariant.copy(0.5f),
                        letterSpacing = 0.8.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}