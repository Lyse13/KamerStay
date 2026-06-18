package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.SearchHotelResult

object SearchResultsMockData {

    val destination = "Douala, Cameroun"
    val totalResults = 87

    val hotels = listOf(
        SearchHotelResult(
            id = "1",
            name = "Sawa Hôtel & SPA",
            location = "Akwa, Douala",
            district = "Akwa",
            rating = 4.9,
            originalPrice = 130_000,
            pricePerNight = 110_000,
            amenities = listOf("Wi-Fi Gratuit", "Piscine Infinie", "SPA & Bien-être"),
            isVerified = true,
            gradientColors = listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A)),
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=800&fit=crop&auto=format"
        ),
        SearchHotelResult(
            id = "2",
            name = "Pullman Hôtel Douala",
            location = "Akwa Nord, Douala",
            district = "Akwa Nord",
            rating = 4.7,
            originalPrice = null,
            pricePerNight = 135_000,
            amenities = listOf("GYM", "Piscine", "Petit-déjeuner inclus"),
            isVerified = true,
            gradientColors = listOf(Color(0xFF2A1A0D), Color(0xFF1A0D06)),
            imageUrl = "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=800&fit=crop&auto=format"
        ),
        SearchHotelResult(
            id = "3",
            name = "Mercure Hôtel Douala",
            location = "Deido, Douala",
            district = "Deido",
            rating = 4.8,
            originalPrice = null,
            pricePerNight = 95_000,
            amenities = listOf("Vue Fleuve Wouri", "Restaurant Gastronomique", "Parking"),
            isVerified = false,
            gradientColors = listOf(Color(0xFF0D3A4A), Color(0xFF062030)),
            imageUrl = "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=800&fit=crop&auto=format"
        ),
        SearchHotelResult(
            id = "4",
            name = "Ibis Styles Douala",
            location = "Bonanjo, Douala",
            district = "Bonanjo",
            rating = 4.6,
            originalPrice = null,
            pricePerNight = 71_000,
            amenities = listOf("Navette Aéroport", "Restaurant", "Wi-Fi Gratuit"),
            isVerified = true,
            gradientColors = listOf(Color(0xFF0D4A3A), Color(0xFF062A20)),
            imageUrl = "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=800&fit=crop&auto=format"
        )
    )
}