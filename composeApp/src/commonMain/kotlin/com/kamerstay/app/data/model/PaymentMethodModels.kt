package com.kamerstay.app.data.model

data class PaymentMethod(
    val id: String,
    val name: String,
    val detail: String,
    val type: PaymentMethodType,
    val isActive: Boolean = false
)

enum class PaymentMethodType {
    BANK, MOBILE_MONEY, CARD
}

data class AlternativeHotel(
    val id: String,
    val name: String,
    val location: String,
    val pricePerNight: Int,
    val tags: List<String>,
    val gradientColors: List<androidx.compose.ui.graphics.Color>
)