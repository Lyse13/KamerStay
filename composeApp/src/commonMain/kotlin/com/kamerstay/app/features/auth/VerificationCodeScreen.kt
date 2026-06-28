package com.kamerstay.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.navigation.Routes
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VerificationCodeScreen(navController: NavController) {

    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.verificationCodeState

    LaunchedEffect(Unit) {
        while (state.timeLeft > 0) {
            kotlinx.coroutines.delay(1000)
            state.timeLeft--
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Verify your email",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Code sent to your email",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary
                )
                Text(
                    text = viewModel.forgotPasswordState.email.ifBlank { "votre email" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(36.dp))

                // OTP Boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(4) { index ->
                        val char = state.code.getOrNull(index)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LocalAppColors.current.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(if (char != null) 12.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (char != null) LocalAppColors.current.textPrimary
                                        else OnSurfaceSecondary.copy(0.3f)
                                    )
                            )
                        }
                    }
                }

                // Hidden input
                OutlinedTextField(
                    value = state.code,
                    onValueChange = { if (it.length <= 4) state.code = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Erreur
                if (state.error != null) {
                    Text(
                        text = state.error ?: "",
                        fontSize = 13.sp,
                        color = androidx.compose.ui.graphics.Color(0xFFE53935),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Bandeau dev : visible uniquement si RESEND_API_KEY n'est pas configurée
                val demoOtp = viewModel.forgotPasswordState.debugOtp
                if (demoOtp.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Primary.copy(0.1f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Code de test : $demoOtp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = OnSurfaceSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Resend code in ", fontSize = 14.sp, color = OnSurfaceSecondary)
                    val minutes = state.timeLeft / 60
                    val seconds = state.timeLeft % 60
                    Text(
                        text = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = LocalAppColors.current.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.verifyResetCode {
                            navController.navigate(Routes.ResetPassword.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = state.code.length == 4 && !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Vérifier",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}