package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class Booking(
    val id: String,
    val hotelName: String,
    val location: String,
    val checkIn: String,
    val checkOut: String,
    val totalPrice: String,
    val status: BookingStatus,
    val rating: Double = 0.0,
    val gradientColors: List<Color>,
    val imageUrl: String = ""
)

enum class BookingStatus {
    CONFIRMED, UPCOMING, PAST, CANCELLED
}
