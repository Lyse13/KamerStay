package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.store.ChatHistoryStore
import com.kamerstay.app.data.store.SessionStore

enum class UserRole { TRAVELER, MANAGER }

object UserSession {
    var fullName by mutableStateOf("")
    var email    by mutableStateOf("")
    var phone    by mutableStateOf("")
    var role     by mutableStateOf(UserRole.TRAVELER)
    var token    by mutableStateOf("")
    var language by mutableStateOf("fr")  // "fr" | "en" — détecté depuis le device au démarrage
    val isLoggedIn get() = token.isNotBlank()

    fun login(
        name: String,
        email: String,
        phone: String = "",
        role: UserRole = UserRole.TRAVELER,
        token: String = "",
        expiresAt: Long = 0L
    ) {
        val resolvedName = name.ifBlank { "Utilisateur KamerStay" }
        this.fullName = resolvedName
        this.email    = email
        this.phone    = phone
        this.role     = role
        this.token    = token
        SessionStore.save(
            token     = token,
            fullName  = resolvedName,
            email     = email,
            phone     = phone,
            role      = role.name,
            expiresAt = expiresAt
        )
    }

    fun logout() {
        ChatHistoryStore.clear()
        fullName = ""; email = ""; phone = ""; role = UserRole.TRAVELER; token = ""
        SessionStore.clear()
    }
}