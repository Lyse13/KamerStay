package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.model.BookingStatus

object BookingsMockData {

    val upcoming = listOf(
        Booking(
            id = "1",
            hotelName = "Azure Bay Resort",
            location = "Santorini, Greece",
            checkIn = "Oct 12",
            checkOut = "Oct 15, 2023",
            totalPrice = "\$1,240.00",
            status = BookingStatus.CONFIRMED,
            gradientColors = listOf(Color(0xFF0D3A5C), Color(0xFF1A6B8A))
        ),
        Booking(
            id = "2",
            hotelName = "Eco-Luxe Garden Suites",
            location = "Bali, Indonesia",
            checkIn = "Dec 24",
            checkOut = "Dec 27, 2023",
            totalPrice = "\$1,560.00",
            status = BookingStatus.UPCOMING,
            gradientColors = listOf(Color(0xFF1A3A1A), Color(0xFF0D2A0D))
        ),
    )

    val past = listOf(
        Booking(
            id = "3",
            hotelName = "Yaoundé Grand Palace",
            location = "Yaoundé, Cameroon",
            checkIn = "Sep 15",
            checkOut = "Sep 18, 2023",
            totalPrice = "\$320.00",
            status = BookingStatus.PAST,
            rating = 4.8,
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF2A1A0D))
        ),
    )

    val cancelled = listOf(
        Booking(
            id = "4",
            hotelName = "Limbe Ecolodge",
            location = "Limbe, Cameroon",
            checkIn = "Aug 02",
            checkOut = "Aug 05, 2023",
            totalPrice = "\$180.00",
            status = BookingStatus.CANCELLED,
            gradientColors = listOf(Color(0xFF2A1A2A), Color(0xFF1A0D1A))
        ),
    )
}