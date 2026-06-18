package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.Booking
import com.kamerstay.app.data.model.BookingStatus

object BookingsMockData {

    val upcoming = listOf(
        Booking(
            id = "1",
            hotelName = "Sawa Hôtel & SPA",
            location = "Douala, Cameroun",
            checkIn = "Oct 12",
            checkOut = "Oct 15, 2024",
            totalPrice = "744 000 FCFA",
            status = BookingStatus.CONFIRMED,
            gradientColors = listOf(Color(0xFF0D3A5C), Color(0xFF1A6B8A)),
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&fit=crop&auto=format"
        ),
        Booking(
            id = "2",
            hotelName = "Mont Fébé Hôtel",
            location = "Yaoundé, Cameroun",
            checkIn = "Déc 24",
            checkOut = "Déc 27, 2024",
            totalPrice = "936 000 FCFA",
            status = BookingStatus.UPCOMING,
            gradientColors = listOf(Color(0xFF1A3A1A), Color(0xFF0D2A0D)),
            imageUrl = "https://images.unsplash.com/photo-1510798831971-661eb04b3739?w=800&fit=crop&auto=format"
        ),
    )

    val past = listOf(
        Booking(
            id = "3",
            hotelName = "Yaoundé Grand Palace",
            location = "Yaoundé, Cameroun",
            checkIn = "Sep 15",
            checkOut = "Sep 18, 2024",
            totalPrice = "192 000 FCFA",
            status = BookingStatus.PAST,
            rating = 4.8,
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF2A1A0D)),
            imageUrl = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=800&fit=crop&auto=format"
        ),
    )

    val cancelled = listOf(
        Booking(
            id = "4",
            hotelName = "Hôtel Seme Beach",
            location = "Limbé, Cameroun",
            checkIn = "Août 02",
            checkOut = "Août 05, 2024",
            totalPrice = "108 000 FCFA",
            status = BookingStatus.CANCELLED,
            gradientColors = listOf(Color(0xFF2A1A2A), Color(0xFF1A0D1A)),
            imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=800&fit=crop&auto=format"
        ),
    )
}