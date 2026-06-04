package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BookingReviewState {
    var fullName by mutableStateOf("Alexander Sterling")
    var email by mutableStateOf("a.sterling@example.com")
    var phone by mutableStateOf("+1 (555) 012-3456")
    var specialRequests by mutableStateOf("High floor, quiet room preferred.")
    var isLoading by mutableStateOf(false)

    val booking = com.kamerstay.app.data.model.BookingReviewData(
        hotelName = "Azure Bay Resort & Spa",
        location = "Mykonos, Greece",
        rating = 4.9,
        amenities = listOf("Wi-Fi", "Pool", "Breakfast"),
        checkIn = "Oct 12",
        checkOut = "Oct 17",
        nights = 5,
        guests = 2,
        rooms = 1,
        roomType = "Deluxe Ocean View",
        roomDetail = "King bed, Balcony",
        pricePerNight = 420.0,
        serviceFee = 45.0,
        taxesFees = 182.50
    )
}