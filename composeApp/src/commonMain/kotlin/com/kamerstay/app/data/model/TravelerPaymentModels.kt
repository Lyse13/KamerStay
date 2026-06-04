package com.kamerstay.app.data.model

data class TravelerCard(
    val id: String,
    val label: String,
    val number: String,
    val expiry: String,
    val type: TravelerCardType,
    val isPrimary: Boolean = false
)

enum class TravelerCardType {
    VISA, MASTERCARD, MOBILE_MONEY_MTN, MOBILE_MONEY_ORANGE
}