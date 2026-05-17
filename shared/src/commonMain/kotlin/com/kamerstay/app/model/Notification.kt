package com.kamerstay.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "",
    val isRead: Boolean = false,
    val createdAt: String = "",
    val referenceId: String? = null
)
