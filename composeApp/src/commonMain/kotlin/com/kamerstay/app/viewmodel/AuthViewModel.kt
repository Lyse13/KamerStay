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
import kotlin.time.Clock

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
                val role = if (response.user.role.name == "HOTEL_MANAGER") UserRole.MANAGER else UserRole.TRAVELER
                UserSession.login(
                    name      = response.user.fullName,
                    email     = response.user.email,
                    phone     = response.user.phoneNumber,
                    role      = role,
                    token     = response.token,
                    expiresAt = Clock.System.now().toEpochMilliseconds() + 24 * 60 * 60 * 1000L
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
                    role        = if (signUpState.selectedRole == UserRole.MANAGER) "MANAGER" else "TRAVELER"
                )
                UserSession.login(
                    name      = response.user.fullName,
                    email     = response.user.email,
                    phone     = response.user.phoneNumber,
                    role      = signUpState.selectedRole,
                    token     = response.token,
                    expiresAt = Clock.System.now().toEpochMilliseconds() + 24 * 60 * 60 * 1000L
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

    fun resetSignUp() {
        signUpState.fullName       = ""
        signUpState.email          = ""
        signUpState.phoneNumber    = ""
        signUpState.password       = ""
        signUpState.passwordVisible = false
        signUpState.submitted      = false
        authError                  = null
    }

    // ── Mot de passe oublié ───────────────────────────────────
    fun sendResetCode(onSuccess: () -> Unit) {
        val email = forgotPasswordState.email.trim()
        if (email.isBlank()) { forgotPasswordState.error = "Entrez votre email."; return }
        viewModelScope.launch {
            forgotPasswordState.isLoading = true
            forgotPasswordState.error = null
            try {
                val response = authRepository.forgotPassword(email)
                forgotPasswordState.debugOtp = response.demoCode
                forgotPasswordState.isEmailSent = true
                onSuccess()
            } catch (_: Exception) {
                forgotPasswordState.error = "Impossible d'envoyer le code. Vérifiez votre connexion."
            } finally {
                forgotPasswordState.isLoading = false
            }
        }
    }

    fun verifyResetCode(onSuccess: () -> Unit) {
        val email = forgotPasswordState.email.trim()
        val code  = verificationCodeState.code.trim()
        if (code.length != 4) { verificationCodeState.error = "Entrez les 4 chiffres."; return }
        viewModelScope.launch {
            isLoading = true
            verificationCodeState.error = null
            try {
                val response = authRepository.verifyCode(email, code)
                if (response.valid && response.resetToken.isNotBlank()) {
                    verificationCodeState.resetToken = response.resetToken
                    verificationCodeState.isVerified = true
                    onSuccess()
                } else {
                    verificationCodeState.error = response.message.ifBlank { "Code incorrect." }
                }
            } catch (_: Exception) {
                verificationCodeState.error = "Vérification impossible. Réessayez."
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePassword(onSuccess: () -> Unit) {
        val state = resetPasswordState
        if (state.newPassword != state.confirmPassword) {
            state.error = "Les mots de passe ne correspondent pas."; return
        }
        if (state.newPassword.length < 8) {
            state.error = "Mot de passe trop court (8 caractères minimum)."; return
        }
        val resetToken = verificationCodeState.resetToken
        if (resetToken.isBlank()) {
            state.error = "Session expirée. Recommencez la procédure."; return
        }
        viewModelScope.launch {
            state.isLoading = true
            state.error = null
            try {
                authRepository.resetPassword(resetToken, state.newPassword)
                state.isSuccess = true
                onSuccess()
            } catch (_: Exception) {
                state.error = "Impossible de mettre à jour le mot de passe. Réessayez."
            } finally {
                state.isLoading = false
            }
        }
    }
}