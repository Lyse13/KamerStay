package com.kamerstay.app.features.auth

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.kamerstay.app.core.components.ErrorPopup
import com.kamerstay.app.core.components.PasswordRequirements
import com.kamerstay.app.core.components.PasswordStrengthIndicator
import com.kamerstay.app.core.components.SignUpLabel
import com.kamerstay.app.core.components.signUpTextFieldColors
import com.kamerstay.app.core.navigation.NavigationState
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.state.UserRole
import com.kamerstay.app.viewmodel.AuthViewModel
import kamerstay.composeapp.generated.resources.Res
import kamerstay.composeapp.generated.resources.kamerstay_logo
import org.jetbrains.compose.resources.painterResource
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

    val year = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.signUpState
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.resetSignUp()
        state.selectedRole = if (NavigationState.pendingRole == "MANAGER") UserRole.MANAGER else UserRole.TRAVELER
    }


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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SecondaryContainer.copy(alpha = 0.35f),
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(Res.drawable.kamerstay_logo),
                    contentDescription = "KamerStay",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                )
            }

            HorizontalDivider(color = SecondaryContainer.copy(alpha = 0.85f))

            Spacer(modifier = Modifier.height(40.dp))

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
                        text = "Create Account",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Start your journey with professional concierge service.",
                        fontSize = 14.sp,
                        color = LocalAppColors.current.textPrimary,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    SignUpLabel("Full Name")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { state.fullName = it },
                        placeholder = {
                            Text("Ex: Lysette Mouandeu", color = OnSurfaceSecondary.copy(0.5f))
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

                    SignUpLabel("Email Address")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { state.email = it },
                        placeholder = {
                            Text("nom@email.com", color = OnSurfaceSecondary.copy(0.5f))
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

                    SignUpLabel("Phone Number")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { state.phoneNumber = it },
                        placeholder = {
                            Text("+(237) 678 90 12 34", color = OnSurfaceSecondary.copy(0.5f))
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

                    if (state.password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordStrengthIndicator(strength = passwordStrength)
                        Spacer(modifier = Modifier.height(12.dp))
                        PasswordRequirements(password = state.password)
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    var isPressed by remember { mutableStateOf(false) }

                    val buttonColor by animateColorAsState(
                        targetValue = if (isPressed) SecondaryContainer else Primary,
                        animationSpec = tween(250),
                        label = "buttonColor"
                    )

                    LaunchedEffect(viewModel.isLoading) {
                        if (!viewModel.isLoading) {
                            isPressed = false
                        }
                    }

                    Button(
                        onClick = {
                            isPressed = true
                            scope.launch {
                                scale.animateTo(0.92f, tween(70))
                                scale.animateTo(1.03f, tween(90))
                                scale.animateTo(
                                    1f,
                                    spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            }

                            viewModel.onAuthSuccess = {
                                navController.navigate(Routes.SignIn.route) {
                                    popUpTo(Routes.Welcome.route) { inclusive = true }
                                }
                            }

                            viewModel.signUp()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .graphicsLayer {
                                scaleX = scale.value
                                scaleY = scale.value
                            },
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor
                        )
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = OnPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Create Account",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnPrimary.copy(alpha = 0.85f)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    viewModel.authError?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        ErrorPopup(message = error)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Sign In Link ───────────────────
                    val annotatedText = buildAnnotatedString {
                        append("Already have an account? ")

                        pushStringAnnotation(
                            tag = "SIGN_IN",
                            annotation = "sign_in"
                        )

                        withStyle(
                            SpanStyle(
                                color = SecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Sign In")
                        }

                        pop()
                    }

                    ClickableText(
                        text = annotatedText,
                        modifier = Modifier.fillMaxWidth(),
                        style = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        ),
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(
                                tag = "SIGN_IN",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let {
                                navController.navigate(Routes.SignIn.route)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Footer ────────────────────────────────
            Text(
                text = "© $year KamerStay Hospitality Group. All rights reserved.",
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