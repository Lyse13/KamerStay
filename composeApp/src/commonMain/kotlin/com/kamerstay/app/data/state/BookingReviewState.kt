package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BookingReviewState {
    var fullName by mutableStateOf(UserSession.fullName.ifBlank { "" })
    var email by mutableStateOf(UserSession.email.ifBlank { "" })
    var phone by mutableStateOf(UserSession.phone.ifBlank { "" })
    var specialRequests by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    var booking by mutableStateOf(
        com.kamerstay.app.data.model.BookingReviewData(
            hotelName = "",
            location = "",
            rating = 0.0,
            amenities = emptyList(),
            checkIn = "",
            checkOut = "",
            nights = 1,
            guests = 1,
            rooms = 1,
            roomType = "",
            roomDetail = "",
            pricePerNight = 0.0,
            serviceFee = 0.0,
            taxesFees = 0.0
        )
    )
}