package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.core.theme.DeepEmerald
import com.kamerstay.app.core.theme.OnSurfaceVariant
import com.kamerstay.app.core.theme.StatusCleaning
import com.kamerstay.app.core.theme.WarmAmber

data class LocalExperience(
    val id: String,
    val title: String,
    val category: String,
    val rating: Double,
    val distance: String,
    val tag: String,
    val tagColor: Color,
    val gradientColors: List<Color>,
    val isExpert: Boolean = false,
    val isFeatured: Boolean = false,
    val actionLabel: String = "BOOK EXPERIENCE"
)

data class LocalExpert(
    val name: String,
    val city: String,
    val initials: String,
    val avatarColor: Color,
    val hasBorder: Boolean = false
)

val mockExperiences = listOf(
    LocalExperience(
        id = "1",
        title = "Mt. Cameroon Trek",
        category = "Adventure",
        rating = 4.8,
        distance = "3.2 km from City Center",
        tag = "NEAR BUEA",
        tagColor = DeepEmerald,
        gradientColors = listOf(Color(0xFF2D4A1E), Color(0xFF1A2E10)),
        actionLabel = "BOOK EXPERIENCE"
    ),
    LocalExperience(
        id = "2",
        title = "Douala Port Safari",
        category = "Historical",
        rating = 4.9,
        distance = "0.5 km from Douala Port",
        tag = "HISTORICAL",
        tagColor = OnSurfaceVariant,
        gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28)),
        isExpert = true,
        actionLabel = "VIEW DETAILS"
    ),
)

val mockExperts = listOf(
    LocalExpert("Jean-Luc", "Douala", "JL", WarmAmber, hasBorder = true),
    LocalExpert("Marie-Sol", "Yaoundé", "MS", DeepEmerald),
    LocalExpert("Papa Eto'o", "Kribi", "PE", Color(0xFF8B6914)),
    LocalExpert("Aminata", "Buea", "AM", StatusCleaning),
)
