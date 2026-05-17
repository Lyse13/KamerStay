package com.kamerstay.app.model

import com.kamerstay.app.model.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val role: UserRole = UserRole.TRAVELER,
    val profileImageUrl: String? = null,
    val createdAt: String = "",
    val isActive: Boolean = true
)
