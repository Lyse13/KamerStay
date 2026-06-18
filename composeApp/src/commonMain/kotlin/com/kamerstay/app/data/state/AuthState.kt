package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SignInState {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var selectedRole by mutableStateOf(UserRole.TRAVELER)
    var passwordVisible by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class SignUpState {
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var submitted by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class ForgotPasswordState {
    var email by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isEmailSent by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class VerificationCodeState {
    var code by mutableStateOf("")
    var timeLeft by mutableStateOf(57)
    var isVerified by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}

class ResetPasswordState {
    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var newPasswordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}
