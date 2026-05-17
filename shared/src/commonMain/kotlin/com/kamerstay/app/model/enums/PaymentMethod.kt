package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod {
    MOBILE_MONEY,
    ORANGE_MONEY,
    CASH,
    CARD
}