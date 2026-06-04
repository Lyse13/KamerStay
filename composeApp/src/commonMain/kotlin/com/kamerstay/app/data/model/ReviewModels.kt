package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class GuestReview(
    val id: String,
    val name: String,
    val initials: String,
    val avatarColor: Color,
    val hasPhoto: Boolean = false,
    val stayType: String,
    val date: String,
    val rating: Double,
    val comment: String,
    val tags: List<String> = emptyList(),
    val hasImages: Boolean = false
)

data class CategoryRating(
    val label: String,
    val score: Double
)