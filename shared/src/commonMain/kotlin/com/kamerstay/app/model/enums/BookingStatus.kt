package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class BookingStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    CHECKED_IN,
    CHECKED_OUT
}