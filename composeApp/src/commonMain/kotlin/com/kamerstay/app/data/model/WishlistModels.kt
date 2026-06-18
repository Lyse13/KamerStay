package com.kamerstay.app.data.model

data class WishlistHotel(
    val id: String,
    val name: String,
    val location: String,
    val region: String,
    val rating: Double,
    val pricePerNight: Int,
    val isFavorite: Boolean = true,
    val gradientColors: List<androidx.compose.ui.graphics.Color>,
    val imageUrl: String = ""
)