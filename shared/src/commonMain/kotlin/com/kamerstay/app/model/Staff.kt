package com.kamerstay.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Staff(
    val id: String = "",
    val hotelId: String = "",
    val fullName: String = "",
    val role: String = "",
    val email: String = "",
    val phone: String = "",
    val permission: String = "View Only",
    val status: String = "ACTIVE",   // ACTIVE | AWAY | OFF_DUTY
    val shift: String = "",
    val createdAt: String = ""
)