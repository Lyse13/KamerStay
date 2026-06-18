package com.kamerstay.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kamerstay.app.data.state.ForgotPasswordState
import com.kamerstay.app.data.state.ResetPasswordState
import com.kamerstay.app.data.state.SignInState
import com.kamerstay.app.data.state.SignUpState
import com.kamerstay.app.data.state.UserRole
import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.data.state.VerificationCodeState
import com.kamerstay.app.features.auth.validateEmail
import com.kamerstay.app.features.auth.validateFullName
import com.kamerstay.app.features.auth.validatePassword
import com.kamerstay.app.features.auth.validatePhone

class AuthViewModel : ViewModel() {
    val signInState = SignInState()
    val signUpState = SignUpState()
    val forgotPasswordState = ForgotPasswordState()
    val verificationCodeState = VerificationCodeState()
    val resetPasswordState = ResetPasswordState()

    fun validateAndSignIn(): Boolean {
        val ok = signInState.email.isNotBlank() && signInState.password.isNotBlank()
        if (ok) {
            // Derive a display name from the email (before @)
            val nameFromEmail = signInState.email
                .substringBefore("@")
                .replace(".", " ")
                .split(" ")
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
            UserSession.login(
                name  = nameFromEmail,
                email = signInState.email,
                role  = signInState.selectedRole
            )
        }
        return ok
    }

    fun validateAndSignUp(): Boolean {
        signUpState.submitted = true
        val ok = validateFullName(signUpState.fullName) == null &&
                validateEmail(signUpState.email) == null &&
                validatePhone(signUpState.phoneNumber) == null &&
                validatePassword(signUpState.password) == null
        if (ok) {
            UserSession.login(
                name  = signUpState.fullName,
                email = signUpState.email,
                phone = signUpState.phoneNumber,
                role  = UserRole.TRAVELER
            )
        }
        return ok
    }
}