package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.WishlistHotel

object WishlistMockData {

    val wishlistHotels = listOf(
        WishlistHotel(
            id = "1",
            name = "Crystal Palace Hotel",
            location = "Douala",
            region = "Littoral",
            rating = 4.9,
            pricePerNight = 85000,
            gradientColors = listOf(Color(0xFF0D4A6A), Color(0xFF1A2A3A))
        ),
        WishlistHotel(
            id = "2",
            name = "The Hilton Heights",
            location = "Yaoundé",
            region = "Centre",
            rating = 4.7,
            pricePerNight = 120000,
            gradientColors = listOf(Color(0xFF1A3A2E), Color(0xFF0D2218))
        ),
        WishlistHotel(
            id = "3",
            name = "Kribi Beach Lounge",
            location = "Kribi",
            region = "South",
            rating = 4.8,
            pricePerNight = 65000,
            gradientColors = listOf(Color(0xFF2A1A0D), Color(0xFF1A0D06))
        )
    )
}