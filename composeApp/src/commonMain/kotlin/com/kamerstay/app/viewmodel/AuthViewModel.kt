package com.kamerstay.app.viewmodel

import androidx.lifecycle.ViewModel
import com.kamerstay.app.data.state.ForgotPasswordState
import com.kamerstay.app.data.state.ResetPasswordState
import com.kamerstay.app.data.state.SignInState
import com.kamerstay.app.data.state.SignUpState
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
        // TODO: appel API
        return signInState.email.isNotBlank() &&
                signInState.password.isNotBlank()
    }

    fun validateAndSignUp(): Boolean {
        signUpState.submitted = true
        return validateFullName(signUpState.fullName) == null &&
                validateEmail(signUpState.email) == null &&
                validatePhone(signUpState.phoneNumber) == null &&
                validatePassword(signUpState.password) == null
    }
}