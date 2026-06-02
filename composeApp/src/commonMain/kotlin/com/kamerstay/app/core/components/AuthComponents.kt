package com.kamerstay.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kamerstay.app.core.theme.*

// ── Form Label ────────────────────────────────────────────
@Composable
fun SignUpLabel(text: String) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextDark,
        modifier = Modifier.fillMaxWidth()
    )
}

// ── Auth TextField Colors (SignIn + ForgotPassword) ───────
@Composable
fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = Color(0xFFDDE4EA),
    focusedLabelColor = Primary,
    cursorColor = Primary,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark,
)

// ── SignUp TextField Colors ───────────────────────────────
@Composable
fun signUpTextFieldColors(isError: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = if (isError) ErrorColor else Primary,
    unfocusedBorderColor = if (isError) ErrorColor.copy(0.5f) else Color(0xFFDDE4EA),
    focusedLabelColor = if (isError) ErrorColor else Primary,
    cursorColor = if (isError) ErrorColor else Primary,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = TextDark,
    unfocusedTextColor = TextDark,
    errorBorderColor = ErrorColor,
    errorContainerColor = ErrorColor.copy(0.04f)
)

// ── Password Strength Indicator ───────────────────────────
@Composable
fun PasswordStrengthIndicator(strength: Int) {
    val (label, color) = when (strength) {
        1 -> "Very Weak" to ErrorColor
        2 -> "Weak" to ElectricBlue
        3 -> "Good" to Primary
        4 -> "Strong" to Secondary
        else -> "" to Color.Transparent
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Password strength", fontSize = 12.sp, color = OnSurfaceSecondary)
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = color)
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
                            else Color(0xFFDDE4EA)
                        )
                )
            }
        }
    }
}

// ── Password Requirements ─────────────────────────────────
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
            .background(Color(0xFFF0FAFB))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "PASSWORD REQUIREMENTS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurfaceSecondary,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        requirements.forEach { (text, met) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (met) Icons.Outlined.CheckCircle
                    else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (met) Primary else OnSurfaceSecondary.copy(0.4f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    fontSize = 12.sp,
                    color = if (met) TextDark else OnSurfaceSecondary
                )
            }
        }
    }
}