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
import androidx.compose.material.icons.filled.*
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
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.model.enums.UserRole

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

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.TRAVELER) }
    var isLoading by remember { mutableStateOf(false) }

    var submitted by remember { mutableStateOf(false) }

    val rawNameError = if (submitted) validateFullName(fullName) else null
    val rawEmailError = if (submitted || email.isNotEmpty()) validateEmail(email) else null
    val rawPhoneError = if (submitted) validatePhone(phoneNumber) else null
    val rawPasswordError = if (submitted || password.isNotEmpty()) validatePassword(password) else null

    // Errors
    val nameError = rawNameError
    val emailError = if (nameError == null) rawEmailError else null
    val phoneError = if (nameError == null && emailError == null) rawPhoneError else null
    val passwordError = if (nameError == null && emailError == null && phoneError == null) rawPasswordError else null

    // Password strength
    val passwordStrength = when {
        password.isEmpty() -> 0
        password.length < 6 -> 1
        password.length < 8 -> 2
        validatePassword(password) == null -> 4
        password.length >= 8 && password.any { it.isUpperCase() } -> 3
        else -> 2
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── Back Button ───────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = OnSurface,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Back",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurface
                    )
                }
            }

            // ── Card principale ───────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Avatar icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PersonAdd,
                            contentDescription = null,
                            tint = DeepEmerald,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Create Your\nAccount",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OnSurface,
                        textAlign = TextAlign.Center,
                        lineHeight = 34.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Join our community of travelers and hosts",
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    RoleSelector(
                        selectedRole = selectedRole,
                        onRoleSelected = { selectedRole = it }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Full Name ─────────────────────────
                    FormLabel(text = "Full Name")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = {
                            Text("Mouandeu Pangop", color = OnSurfaceVariant.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Person, contentDescription = null,
                                tint = if (nameError != null) ErrorColor else OnSurfaceVariant)
                        },
                        isError = nameError != null,
                        supportingText = if (nameError != null) {
                            { ErrorPopup(message = nameError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = validatedTextFieldColors(nameError != null),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Email ─────────────────────────────
                    FormLabel(text = "Email")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text("lysette@example.cm", color = OnSurfaceVariant.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Email, contentDescription = null,
                                tint = if (emailError != null) ErrorColor else OnSurfaceVariant)
                        },
                        isError = emailError != null,
                        supportingText = if (emailError != null) {
                            { ErrorPopup(message = emailError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = validatedTextFieldColors(emailError != null),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Phone ─────────────────────────────
                    FormLabel(text = "Phone Number")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = {
                            Text("+237 600 000 000", color = OnSurfaceVariant.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Phone, contentDescription = null,
                                tint = if (phoneError != null) ErrorColor else OnSurfaceVariant)
                        },
                        isError = phoneError != null,
                        supportingText = if (phoneError != null) {
                            { ErrorPopup(message = phoneError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = validatedTextFieldColors(phoneError != null),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Password ──────────────────────────
                    FormLabel(text = "Password")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text("••••••••", color = OnSurfaceVariant.copy(0.5f))
                        },
                        leadingIcon = {
                            Icon(Icons.Outlined.Lock, contentDescription = null,
                                tint = if (passwordError != null) ErrorColor else OnSurfaceVariant)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                        isError = passwordError != null,
                        supportingText = if (passwordError != null) {
                            { ErrorPopup(message = passwordError) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = validatedTextFieldColors(passwordError != null),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    // ── Password Strength Bar ─────────────
                    if (password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        PasswordStrengthIndicator(strength = passwordStrength)
                    }

                    // ── Password Requirements ─────────────
                    if (password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        PasswordRequirements(password = password)
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Create Account Button ─────────────
                    Button(
                        onClick = {
                            submitted = true
                            val isValid = validateFullName(fullName) == null &&
                                    validateEmail(email) == null &&
                                    validatePhone(phoneNumber) == null &&
                                    validatePassword(password) == null
                            if (isValid) {
                                isLoading = true
                                // TODO: navigate or call API
                            }
                        },
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
                                text = "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = OnSurfaceVariant)) {
                                append("Already have an account? ")
                            }
                            withStyle(SpanStyle(
                                color = DeepEmerald,
                                fontWeight = FontWeight.Bold
                            )) {
                                append("Sign In")
                            }
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.clickable {
                            navController.navigate(Routes.SignIn.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Divider)
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.Shield, contentDescription = null,
                            tint = WarmAmber, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("SECURE REGISTRATION", fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(modifier = Modifier.size(4.dp).clip(CircleShape)
                            .background(OnSurfaceVariant.copy(0.3f)))
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Outlined.Shield, contentDescription = null,
                            tint = WarmAmber, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("DATA PRIVACY", fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant,
                            letterSpacing = 0.8.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "KamerStay",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceVariant.copy(0.4f)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoleSelector(
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Traveler
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedRole == UserRole.TRAVELER) DeepEmerald
                        else Color.Transparent
                    )
                    .clickable { onRoleSelected(UserRole.TRAVELER) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.TravelExplore,
                        contentDescription = null,
                        tint = if (selectedRole == UserRole.TRAVELER)
                            Color.White else OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Traveler",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedRole == UserRole.TRAVELER)
                            Color.White else OnSurfaceVariant
                    )
                }
            }

            // Hotel Manager
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (selectedRole == UserRole.HOTEL_MANAGER) DeepEmerald
                        else Color.Transparent
                    )
                    .clickable { onRoleSelected(UserRole.HOTEL_MANAGER) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Business,
                        contentDescription = null,
                        tint = if (selectedRole == UserRole.HOTEL_MANAGER)
                            Color.White else OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hotel Manager",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selectedRole == UserRole.HOTEL_MANAGER)
                            Color.White else OnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordStrengthIndicator(strength: Int) {
    val (label, color) = when (strength) {
        1 -> "Very Weak" to ErrorColor
        2 -> "Weak" to StatusReserved
        3 -> "Good" to WarmAmber
        4 -> "Strong" to StatusConfirmed
        else -> "" to Color.Transparent
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Password strength",
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (index < strength) color
                            else OutlineVariant.copy(alpha = 0.3f)
                        )
                )
            }
        }
    }
}

@Composable
fun PasswordRequirements(password: String) {
    val requirements = listOf(
        "At least 8 characters" to (password.length >= 8),
        "One uppercase letter (A-Z)" to password.any { it.isUpperCase() },
        "One lowercase letter (a-z)" to password.any { it.isLowerCase() },
        "One number (0-9)" to password.any { it.isDigit() },
        "One special character (@, #, \$, !...)" to password.any { !it.isLetterOrDigit() }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceVariant)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "PASSWORD REQUIREMENTS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurfaceVariant,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        requirements.forEach { (text, met) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (met) Icons.Outlined.CheckCircle
                    else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (met) StatusConfirmed else OnSurfaceVariant.copy(0.4f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = if (met) OnSurface else OnSurfaceVariant
                )
            }
        }
    }
}

// ── Form Label ────────────────────────────────────────────
@Composable
fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = OnSurface,
        modifier = Modifier.fillMaxWidth()
    )
}

// ── TextField Colors ──────────────────────────────────────
@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DeepEmerald,
    unfocusedBorderColor = OutlineVariant,
    focusedLabelColor = DeepEmerald,
    cursorColor = DeepEmerald,
    focusedContainerColor = SurfaceVariant,
    unfocusedContainerColor = SurfaceVariant,
)

// ── Validated TextField Colors ────────────────────────────
@Composable
fun validatedTextFieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = if (isError) ErrorColor else DeepEmerald,
    unfocusedBorderColor = if (isError) ErrorColor.copy(0.5f) else OutlineVariant,
    focusedLabelColor = if (isError) ErrorColor else DeepEmerald,
    cursorColor = if (isError) ErrorColor else DeepEmerald,
    focusedContainerColor = if (isError) ErrorColor.copy(0.04f) else SurfaceVariant,
    unfocusedContainerColor = if (isError) ErrorColor.copy(0.04f) else SurfaceVariant,
    errorBorderColor = ErrorColor,
    errorContainerColor = ErrorColor.copy(0.04f),
)