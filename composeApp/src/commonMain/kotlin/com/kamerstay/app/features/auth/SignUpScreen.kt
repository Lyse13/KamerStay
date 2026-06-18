package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.kamerstay.app.core.components.PasswordRequirements
import com.kamerstay.app.core.components.PasswordStrengthIndicator
import com.kamerstay.app.core.components.SignUpLabel
import com.kamerstay.app.core.components.signUpTextFieldColors
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.enums.UserRole
import com.kamerstay.app.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

// ── Validation helpers ────────────────────────────────────
fun validateFullName(name: String): String? {
    return when {
        name.isBlank() -> "Please fill out this field."
        name.trim().length < 2 -> "Name must be at least 2 characters."
        else -> null
    }
}

fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Please fill out this field."
        !email.contains("@") -> "Please include an '@' in the email address. '$email' is missing an '@'."
        email.endsWith("@") || email.substringAfter("@").isBlank() ->
            "Please enter a part following '@'. '$email' is incomplete."
        !email.substringAfter("@").contains(".") ->
            "Please enter a valid email address."
        else -> null
    }
}

fun validatePhone(phone: String): String? {
    return when {
        phone.isBlank() -> "Please fill out this field."
        phone.replace(" ", "").replace("+", "").length < 9 ->
            "Please enter a valid phone number."
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Please fill out this field."
        password.length < 8 -> "Password must be at least 8 characters."
        !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter."
        !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter."
        !password.any { it.isDigit() } -> "Password must contain at least one number."
        !password.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character (@, #, \$, etc.)."
        else -> null
    }
}


@Composable
fun SignUpScreen(navController: NavController) {

    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.signUpState

    val nameError = if (state.submitted) validateFullName(state.fullName) else null
    val emailError = if (state.submitted || state.email.isNotEmpty())
        validateEmail(state.email) else null
    val phoneError = if (state.submitted) validatePhone(state.phoneNumber) else null
    val passwordError = if (state.submitted || state.password.isNotEmpty())
        validatePassword(state.password) else null

    val passwordStrength = when {
        state.password.isEmpty() -> 0
        state.password.length < 6 -> 1
        state.password.length < 8 -> 2
        validatePassword(state.password) == null -> 4
        state.password.length >= 8 && state.password.any { it.isUpperCase() } -> 3
        else -> 2
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
            // ── Top Bar ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "KamerStay",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F4F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            HorizontalDivider(color = Divider)

            Spacer(modifier = Modifier.height(24.dp))

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
                    // ── Header ────────────────────────
                    Text(
                        text = "Create Account",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start your journey with professional concierge service.",
                        fontSize = 14.sp,
                        color = OnSurfaceSecondary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Full Name ─────────────────────
                    SignUpLabel("Full Name")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { state.fullName = it },
                        placeholder = {
                            Text("John Doe", color = OnSurfaceSecondary.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Badge,
                                contentDescription = null,
                                tint = if (nameError != null) ErrorColor
                                else OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        isError = nameError != null,
                        supportingText = if (nameError != null) {
                            { ErrorPopup(message = nameError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = signUpTextFieldColors(nameError != null),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Email ─────────────────────────
                    SignUpLabel("Email Address")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { state.email = it },
                        placeholder = {
                            Text("john@example.com", color = OnSurfaceSecondary.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.MailOutline,
                                contentDescription = null,
                                tint = if (emailError != null) ErrorColor
                                else OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        isError = emailError != null,
                        supportingText = if (emailError != null) {
                            { ErrorPopup(message = emailError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = signUpTextFieldColors(emailError != null),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Phone ─────────────────────────
                    SignUpLabel("Phone Number")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { state.phoneNumber = it },
                        placeholder = {
                            Text("+1(555)000-0000", color = OnSurfaceSecondary.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Phone,
                                contentDescription = null,
                                tint = if (phoneError != null) ErrorColor
                                else OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        isError = phoneError != null,
                        supportingText = if (phoneError != null) {
                            { ErrorPopup(message = phoneError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = signUpTextFieldColors(phoneError != null),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Password ──────────────────────
                    SignUpLabel("Password")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { state.password = it },
                        placeholder = {
                            Text("••••••••", color = OnSurfaceSecondary.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = if (passwordError != null) ErrorColor
                                else OnSurfaceSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { state.passwordVisible = !state.passwordVisible }) {
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
                        isError = passwordError != null,
                        supportingText = if (passwordError != null) {
                            { ErrorPopup(message = passwordError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = signUpTextFieldColors(passwordError != null),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        singleLine = true
                    )

                    // Password strength
                    if (state.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordStrengthIndicator(strength = passwordStrength)
                        Spacer(modifier = Modifier.height(12.dp))
                        PasswordRequirements(password = state.password)
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Create Account Button ──────────
                    Button(
                        onClick = {
                            state.submitted = true
                            if (viewModel.validateAndSignUp()) {
                                state.isLoading = true
                                navController.navigate(Routes.TravelerHome.route) {
                                    popUpTo(Routes.Welcome.route) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = OnPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Create Account  →",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Sign In Link ───────────────────
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = OnSurfaceSecondary)) {
                                append("Already have an account? ")
                            }
                            withStyle(SpanStyle(
                                color = Secondary,
                                fontWeight = FontWeight.Bold
                            )) {
                                append("Sign In")
                            }
                        },
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Routes.SignIn.route) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Footer ────────────────────────────────
            Text(
                text = "© 2024 KamerStay Hospitality Group. All rights reserved.",
                fontSize = 12.sp,
                color = OnSurfaceSecondary.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}