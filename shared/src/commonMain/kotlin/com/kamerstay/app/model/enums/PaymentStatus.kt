package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus {
    PENDING,
    DEPOSIT_PAID,
    FULLY_PAID,
    REFUNDED
}