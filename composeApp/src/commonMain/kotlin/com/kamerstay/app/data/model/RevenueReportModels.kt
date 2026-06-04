package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class RevenueMetric(
    val id: String,
    val icon: String,
    val label: String,
    val value: String,
    val change: String,
    val isPositive: Boolean
)

data class MonthlyEarning(
    val month: String,
    val revenueHeight: Float,
    val guestsHeight: Float,
    val isHighlighted: Boolean = false,
    val highlightLabel: String = ""
)

data class PaymentMethodShare(
    val icon: String,
    val name: String,
    val percentage: Int
)

data class RecentTransaction(
    val id: String,
    val guestName: String,
    val roomType: String,
    val nights: Int,
    val date: String,
    val status: TransactionStatus,
    val amount: String,
    val initials: String,
    val avatarColor: Color
)

enum class TransactionStatus {
    COMPLETED, PENDING, FAILED
}