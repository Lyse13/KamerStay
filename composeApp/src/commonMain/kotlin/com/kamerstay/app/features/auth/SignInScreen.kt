package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.kamerstay.app.core.components.ErrorPopup
import com.kamerstay.app.core.components.SignUpLabel
import com.kamerstay.app.core.components.authTextFieldColors
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.state.UserRole
import com.kamerstay.app.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SignInScreen(navController: NavController) {

    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.signInState
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0F7FA),
                        BackgroundLight,
                        Color(0xFFE8F4F5)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KamerStay",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Main Card ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(LocalAppColors.current.surface)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column {
                    Text(
                        text = "Bienvenue",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Connectez-vous à votre compte KamerStay",
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Sélecteur de rôle ─────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LocalAppColors.current.background),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        listOf(UserRole.TRAVELER to "Voyageur", UserRole.MANAGER to "Manager").forEach { (role, label) ->
                            val isSelected = state.selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Secondary else Color.Transparent)
                                    .clickable { state.selectedRole = role }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else OnSurfaceSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Email ─────────────────────────
                    SignUpLabel("Adresse Email")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.email,              // ← state
                        onValueChange = { state.email = it }, // ← state
                        placeholder = {
                            Text(
                                "name@example.com",
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.MailOutline,
                                contentDescription = null,
                                tint = OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
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

                    // ── Password ──────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SignUpLabel("Password")
                        Text(
                            text = "Forgot Password?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondary,
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.ForgotPassword.route)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.password,               // ← state
                        onValueChange = { state.password = it }, // ← state
                        placeholder = {
                            Text(
                                "••••••••",
                                color = OnSurfaceSecondary.copy(0.5f)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    state.passwordVisible = !state.passwordVisible // ← state
                                }
                            ) {
                                Icon(
                                    imageVector = if (state.passwordVisible)
                                        Icons.Outlined.VisibilityOff
                                    else Icons.Outlined.Visibility,
                                    contentDescription = null,
                                    tint = OnSurfaceSecondary
                                )
                            }
                        },
                        visualTransformation = if (state.passwordVisible)
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

                    // ── Sign In Button ────────────────
                    Button(
                        onClick = {
                            if (viewModel.validateAndSignIn()) {
                                val dest = if (state.selectedRole == UserRole.MANAGER)
                                    Routes.ManagerDashboard.route
                                else
                                    Routes.TravelerHome.route
                                navController.navigate(dest) {
                                    popUpTo(Routes.Welcome.route) { inclusive = true }
                                }
                            } else {
                                state.error = "Veuillez remplir tous les champs."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Secondary
                        )
                    ) {
                        if (state.isLoading) {  // ← state
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    // ── Error message ─────────────────
                    state.error?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorPopup(message = error)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = OnSurfaceSecondary)) {
                                append("Don't have an account? ")
                            }
                            withStyle(SpanStyle(
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )) {
                                append("Create Account")
                            }
                        },
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Routes.SignUp.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFCFD8DC).copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }
    }
}