package com.kamerstay.app.model

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val id: String = "",
    val userId: String = "",
    val email: String = "",
    val passwordHash: String = ""
)
