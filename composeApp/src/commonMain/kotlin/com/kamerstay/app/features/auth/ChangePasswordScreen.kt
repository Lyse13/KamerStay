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
import com.kamerstay.app.core.theme.*

@Composable
fun ChangePasswordScreen(navController: NavController) {

    var currentPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasNumberOrSymbol = newPassword.any { it.isDigit() || !it.isLetterOrDigit() }

    val passwordStrength = when {
        newPassword.isEmpty() -> 0f
        !hasMinLength -> 0.25f
        hasMinLength && !hasUppercase -> 0.5f
        hasMinLength && hasUppercase && !hasNumberOrSymbol -> 0.75f
        else -> 1f
    }

    val strengthLabel = when {
        newPassword.isEmpty() -> ""
        passwordStrength <= 0.25f -> "Weak"
        passwordStrength <= 0.5f -> "Fair"
        passwordStrength <= 0.75f -> "Good"
        else -> "Strong"
    }

    val strengthColor = when {
        newPassword.isEmpty() -> OnSurfaceSecondary
        passwordStrength <= 0.25f -> ErrorColor
        passwordStrength <= 0.5f -> ElectricBlue
        passwordStrength <= 0.75f -> Primary
        else -> Secondary
    }

    val canSubmit = currentPassword.isNotBlank() &&
            hasMinLength && hasUppercase && hasNumberOrSymbol &&
            newPassword == confirmPassword

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = LocalAppColors.current.surface,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = "Password Updated",
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textPrimary
                )
            },
            text = {
                Text(
                    text = "Your password has been changed successfully.",
                    color = OnSurfaceSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp)
                ) { Text("Done", color = OnPrimary) }
            }
        )
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
            // ── Top Bar ───────────────────────────────────
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
                    text = "Change Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter your current password, then choose a strong new password to secure your account.",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Current Password ──────────────────────
                SignUpLabel("Current Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    placeholder = { Text("Your current password", color = OnSurfaceSecondary.copy(0.5f)) },
                    trailingIcon = {
                        IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                            Icon(
                                if (currentPasswordVisible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = OnSurfaceSecondary
                            )
                        }
                    },
                    visualTransformation = if (currentPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Forgot password hint
                Text(
                    text = "Forgot current password?",
                    fontSize = 13.sp,
                    color = Secondary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(color = Divider)

                Spacer(modifier = Modifier.height(24.dp))

                // ── New Password ──────────────────────────
                SignUpLabel("New Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("••••••••", color = OnSurfaceSecondary.copy(0.5f)) },
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                if (newPasswordVisible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = OnSurfaceSecondary
                            )
                        }
                    },
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true
                )

                if (newPassword.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Password Strength", fontSize = 12.sp, color = OnSurfaceSecondary)
                        Text(
                            strengthLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = strengthColor
                        )
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
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        ResetRequirement("8+ characters", hasMinLength)
                        ResetRequirement("One uppercase letter", hasUppercase)
                        ResetRequirement("One number or symbol", hasNumberOrSymbol)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Confirm Password ──────────────────────
                SignUpLabel("Confirm New Password")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("••••••••", color = OnSurfaceSecondary.copy(0.5f)) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                if (confirmPasswordVisible) Icons.Outlined.VisibilityOff
                                else Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = OnSurfaceSecondary
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    isError = confirmPassword.isNotEmpty() && confirmPassword != newPassword,
                    supportingText = if (confirmPassword.isNotEmpty() && confirmPassword != newPassword) {
                        { Text("Passwords do not match", color = ErrorColor, fontSize = 12.sp) }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = authTextFieldColors(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Update Button ─────────────────────────
                Button(
                    onClick = { showSuccessDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = canSubmit
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

                // ── Security note ─────────────────────────
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