package com.kamerstay.app.data.model

data class SearchHotelResult(
    val id: String,
    val name: String,
    val location: String,
    val district: String,
    val rating: Double,
    val originalPrice: Int?,
    val pricePerNight: Int,
    val amenities: List<String>,
    val isVerified: Boolean = false,
    val isFavorite: Boolean = false,
    val gradientColors: List<androidx.compose.ui.graphics.Color>,
    val imageUrl: String = ""
)
