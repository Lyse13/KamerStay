package com.kamerstay.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamerstay.app.data.remote.AuthRemoteRepository
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
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRemoteRepository()

    val signInState = SignInState()
    val signUpState = SignUpState()
    val forgotPasswordState = ForgotPasswordState()
    val verificationCodeState = VerificationCodeState()
    val resetPasswordState = ResetPasswordState()

    // État du chargement et des erreurs
    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    // Callback appelé quand login/register réussit — la navigation se fait dans le composable
    var onAuthSuccess: (() -> Unit)? = null

    fun signIn() {
        if (signInState.email.isBlank() || signInState.password.isBlank()) {
            authError = "Email et mot de passe requis"
            return
        }
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                val response = authRepository.login(
                    email = signInState.email,
                    password = signInState.password
                )
                val role = if (response.user.role.name == "MANAGER") UserRole.MANAGER else UserRole.TRAVELER
                UserSession.login(
                    name  = response.user.fullName,
                    email = response.user.email,
                    phone = response.user.phoneNumber,
                    role  = role,
                    token = response.token
                )
                onAuthSuccess?.invoke()
            } catch (e: Exception) {
                authError = "Email ou mot de passe incorrect"
            } finally {
                isLoading = false
            }
        }
    }

    fun signUp() {
        signUpState.submitted = true
        val hasError = validateFullName(signUpState.fullName) != null ||
                validateEmail(signUpState.email) != null ||
                validatePhone(signUpState.phoneNumber) != null ||
                validatePassword(signUpState.password) != null
        if (hasError) return

        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                val response = authRepository.register(
                    fullName    = signUpState.fullName,
                    email       = signUpState.email,
                    phoneNumber = signUpState.phoneNumber,
                    password    = signUpState.password,
                    role        = "TRAVELER"
                )
                UserSession.login(
                    name  = response.user.fullName,
                    email = response.user.email,
                    phone = response.user.phoneNumber,
                    role  = UserRole.TRAVELER,
                    token = response.token
                )
                onAuthSuccess?.invoke()
            } catch (e: Exception) {
                authError = when {
                    e.message?.contains("409") == true -> "Un compte existe déjà avec cet email"
                    else -> "Erreur lors de la création du compte. Réessayez."
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        authError = null
    }
}