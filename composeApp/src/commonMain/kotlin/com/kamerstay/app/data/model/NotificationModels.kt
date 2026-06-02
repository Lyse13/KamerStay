package com.kamerstay.app.data.model

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val hasAction: Boolean = false,
    val actionLabel: String = "",
    val isAlert: Boolean = false
)

enum class NotificationType {
    BOOKING, CHECK_IN, ALERT, PROMO, PAYMENT
}