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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
fun ForgotPasswordScreen(navController: NavController) {

    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.forgotPasswordState

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
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Secondary
                    )
                }
                Text(
                    text = "KamerStay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFB0E8EC),
                                        Color(0xFF7DE5FA)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Key,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Forgot Password",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LocalAppColors.current.textPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Don't worry! Enter your email below to receive a password reset link.",
                    fontSize = 14.sp,
                    color = OnSurfaceSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                SignUpLabel("Email Address")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { state.email = it },
                    placeholder = {
                        Text(
                            "e.g. name@example.com",
                            color = OnSurfaceSecondary.copy(0.5f)
                        )
                    },
                    leadingIcon = {
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

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        state.isLoading = true
                        navController.navigate(Routes.VerificationCode.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = state.email.isNotBlank()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = OnPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Send Reset Link",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = OnSurfaceSecondary)) {
                            append("Remembered it? ")
                        }
                        withStyle(SpanStyle(
                            color = Secondary,
                            fontWeight = FontWeight.Bold
                        )) {
                            append("Login")
                        }
                    },
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SignIn.route)
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "© 2024 KamerStay Hospitality Group",
                    fontSize = 12.sp,
                    color = OnSurfaceSecondary.copy(0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

