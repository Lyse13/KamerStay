package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.SearchHotelResult

object SearchResultsMockData {

    val destination = "Abidjan, Ivory Coast"
    val totalResults = 124

    val hotels = listOf(
        SearchHotelResult(
            id = "1",
            name = "Sofitel Abidjan Hotel Ivoire",
            location = "Cocody, Abidjan",
            district = "Cocody",
            rating = 4.9,
            originalPrice = 125000,
            pricePerNight = 95000,
            amenities = listOf("Free Wi-Fi", "Infinity Pool", "Spa"),
            isVerified = true,
            gradientColors = listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A))
        ),
        SearchHotelResult(
            id = "2",
            name = "Azalai Hotel Abidjan",
            location = "Marcory, Abidjan",
            district = "Marcory",
            rating = 4.7,
            originalPrice = null,
            pricePerNight = 68500,
            amenities = listOf("Gym", "Breakfast Inc."),
            isVerified = true,
            gradientColors = listOf(Color(0xFF2A1A0D), Color(0xFF1A0D06))
        ),
        SearchHotelResult(
            id = "3",
            name = "Noom Hotel Abidjan Plateau",
            location = "Plateau, Abidjan",
            district = "Plateau",
            rating = 4.8,
            originalPrice = null,
            pricePerNight = 82000,
            amenities = listOf("City View", "Parking"),
            isVerified = false,
            gradientColors = listOf(Color(0xFF0D3A4A), Color(0xFF062030))
        ),
        SearchHotelResult(
            id = "4",
            name = "Radisson Blu Hotel Airport",
            location = "Port Bouet, Abidjan",
            district = "Port Bouet",
            rating = 4.6,
            originalPrice = null,
            pricePerNight = 74000,
            amenities = listOf("Shuttle", "Beach Access"),
            isVerified = true,
            gradientColors = listOf(Color(0xFF0D4A3A), Color(0xFF062A20))
        )
    )
}