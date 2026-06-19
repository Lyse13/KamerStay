package com.kamerstay.app.model.dto

import com.kamerstay.app.model.User
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val role: String // "TRAVELER" ou "MANAGER"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: User
)

@Serializable
data class ErrorResponse(
    val message: String
)
