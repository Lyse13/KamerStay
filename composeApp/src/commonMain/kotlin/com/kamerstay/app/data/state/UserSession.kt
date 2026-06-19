package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class UserRole { TRAVELER, MANAGER }

object UserSession {
    var fullName by mutableStateOf("")
    var email    by mutableStateOf("")
    var phone    by mutableStateOf("")
    var role     by mutableStateOf(UserRole.TRAVELER)
    var token    by mutableStateOf("")
    val isLoggedIn get() = token.isNotBlank()

    fun login(
        name: String,
        email: String,
        phone: String = "",
        role: UserRole = UserRole.TRAVELER,
        token: String = ""
    ) {
        this.fullName = name.ifBlank { "Utilisateur KamerStay" }
        this.email    = email
        this.phone    = phone
        this.role     = role
        this.token    = token
    }

    fun logout() {
        fullName = ""; email = ""; phone = ""; role = UserRole.TRAVELER; token = ""
    }
}