package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class Landmark(
    val id: String,
    val name: String,
    val description: String,
    val rating: Double,
    val reviewCount: String,
    val price: String,
    val gradientColors: List<Color>,
    val imageUrl: String = ""
)

data class FoodPlace(
    val id: String,
    val name: String,
    val location: String,
    val tag: String,
    val tagColor: Color,
    val gradientColors: List<Color>,
    val imageUrl: String = ""
)

data class ShoppingPlace(
    val id: String,
    val name: String,
    val description: String,
    val gradientColors: List<Color>,
    val imageUrl: String = ""
)

data class NightlifePlace(
    val id: String,
    val name: String,
    val subtitle: String,
    val gradientColors: List<Color>,
    val imageUrl: String = ""
)
