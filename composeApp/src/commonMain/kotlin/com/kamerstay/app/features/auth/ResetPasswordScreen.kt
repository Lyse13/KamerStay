package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.components.SignUpLabel
import com.kamerstay.app.core.components.authTextFieldColors
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResetPasswordScreen(navController: NavController) {

    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.resetPasswordState

    val hasMinLength = state.newPassword.length >= 8
    val hasUppercase = state.newPassword.any { it.isUpperCase() }
    val hasNumberOrSymbol = state.newPassword.any {
        it.isDigit() || !it.isLetterOrDigit()
    }

    val passwordStrength = when {
        state.newPassword.isEmpty() -> 0f
        !hasMinLength -> 0.25f
        hasMinLength && !hasUppercase -> 0.5f
        hasMinLength && hasUppercase && !hasNumberOrSymbol -> 0.75f
        else -> 1f
    }

    val strengthLabel = when {
        state.newPassword.isEmpty() -> "Empty"
        passwordStrength <= 0.25f -> "Weak"
        passwordStrength <= 0.5f -> "Fair"
        passwordStrength <= 0.75f -> "Good"
        else -> "Strong"
    }

    val strengthColor = when {
        state.newPassword.isEmpty() -> OnSurfaceSecondary
        passwordStrength <= 0.25f -> ErrorColor
        passwordStrength <= 0.5f -> ElectricBlue
        passwordStrength <= 0.75f -> Primary
        else -> Secondary
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LocalAppColors.current.surface)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Secondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "KamerStay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
                Spacer(modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                Text(
                    text = "Reset Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Secure your account by choosing a strong new password for your future travels.",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // New Password
                SignUpLabel("New Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.newPassword,
                    onValueChange = { state.newPassword = it },
                    placeholder = {
                        Text("••••••••", color = OnSurfaceSecondary.copy(0.5f))
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            state.newPasswordVisible = !state.newPasswordVisible
                        }) {
                            Icon(
                                if (state.newPasswordVisible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = OnSurfaceSecondary
                            )
                        }
                    },
                    visualTransformation = if (state.newPasswordVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Strength bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Password Strength", fontSize = 12.sp, color = OnSurfaceSecondary)
                    Text(strengthLabel, fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, color = strengthColor)
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { passwordStrength },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = strengthColor,
                    trackColor = Divider
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    ResetRequirement("8+ characters", hasMinLength)
                    ResetRequirement("One uppercase letter", hasUppercase)
                    ResetRequirement("One number or symbol", hasNumberOrSymbol)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password
                SignUpLabel("Confirm Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = { state.confirmPassword = it },
                    placeholder = {
                        Text("••••••••", color = OnSurfaceSecondary.copy(0.5f))
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            state.confirmPasswordVisible = !state.confirmPasswordVisible
                        }) {
                            Icon(
                                if (state.confirmPasswordVisible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = OnSurfaceSecondary
                            )
                        }
                    },
                    visualTransformation = if (state.confirmPasswordVisible)
                        VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Update Password Button
                Button(
                    onClick = {
                        navController.navigate(Routes.PasswordResetSuccess.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = hasMinLength && hasUppercase && hasNumberOrSymbol &&
                            state.newPassword == state.confirmPassword
                ) {
                    Text(
                        text = "Update Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.LockReset,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Data Protection Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(LocalAppColors.current.surface)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Primary.copy(0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Data Protection",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = LocalAppColors.current.textPrimary
                            )
                            Text(
                                text = "Your security is our priority. We use 256-bit encryption to protect your data.",
                                fontSize = 12.sp,
                                color = OnSurfaceSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ResetRequirement(text: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (isMet) Icons.Outlined.CheckCircle
            else Icons.Outlined.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isMet) Primary else OnSurfaceSecondary.copy(0.4f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = if (isMet) LocalAppColors.current.textPrimary else OnSurfaceSecondary
        )
    }
}