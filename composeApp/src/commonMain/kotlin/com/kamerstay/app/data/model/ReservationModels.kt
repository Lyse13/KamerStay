package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class ReservationDetail(
    val id: String,
    val guestName: String,
    val guestInitials: String,
    val reservationId: String,
    val status: String,
    val checkIn: String,
    val checkOut: String,
    val nights: Int,
    val roomName: String,
    val roomDetails: String,
    val phoneNumber: String,
    val email: String,
    val membershipTier: String,
    val specialRequests: String,
    val requestTags: List<String>,
    val roomCharge: String,
    val serviceFees: String,
    val amenitiesAddOn: String,
    val totalAmount: String,
    val paymentStatus: String,
    val cardInfo: String
)

// ── Pour ReservationsScreen ───────────────────────────────
data class Reservation(
    val id: String,
    val guestName: String,
    val guestInitials: String,
    val bookingId: String,
    val roomType: String,
    val roomTag: String,
    val checkIn: String,
    val checkOut: String,
    val nights: Int,
    val status: String,
    val gradientColors: List<Color>
)

