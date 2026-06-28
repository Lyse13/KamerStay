package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class CheckInGuest(
    val id: String,
    val name: String,
    val bookingId: String,
    val room: String,
    val tag: String?,
    val tagColor: Color,
    val arrivalTime: String? = null,
    val paymentPending: Boolean = false,
    val checkedInTime: String? = null,
    val isCheckedIn: Boolean = false
)

data class DepartureGuest(
    val initials: String,
    val name: String,
    val room: String,
    val nights: String,
    val dates: String,
    val balance: String,
    val balanceLabel: String,
    val isPaid: Boolean,
    val bookingId: String = ""
)
