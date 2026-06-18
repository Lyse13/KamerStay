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

data class ManagerNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: ManagerNotificationType,
    val isRead: Boolean = false,
    val hasAction: Boolean = false,
    val actionLabel: String = "",
    val isAlert: Boolean = false
)

enum class ManagerNotificationType {
    NEW_BOOKING, CHECK_IN, CHECK_OUT, PAYMENT, REVIEW, ALERT, STAFF
}