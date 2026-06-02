package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.*

object GuideMockData {

    val landmarks = listOf(
        Landmark(
            id = "1",
            name = "Limbe Botanic Garden",
            description = "Founded in 1892, this lush sanctuary offers a peaceful escape into Cameroon's...",
            rating = 4.9,
            reviewCount = "1.2k",
            price = "\$15",
            gradientColors = listOf(Color(0xFF1A3A1A), Color(0xFF0D280D))
        ),
        Landmark(
            id = "2",
            name = "Monument de la Réunification",
            description = "A powerful symbol of the nation's history, set within beautifully landscaped ground...",
            rating = 4.7,
            reviewCount = "890",
            price = "Free",
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF1A1208))
        ),
    )

    val foodPlaces = listOf(
        FoodPlace(
            id = "1",
            name = "Le Panoramique",
            location = "Akwa, Douala",
            tag = "FINE DINING",
            tagColor = Color(0xFF003761),
            gradientColors = listOf(Color(0xFF1A1A3A), Color(0xFF0D0D28))
        ),
        FoodPlace(
            id = "2",
            name = "Marché Central",
            location = "Central Market",
            tag = "STREET FOOD",
            tagColor = Color(0xFF8B4500),
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF28180D))
        ),
    )

    val shoppingPlaces = listOf(
        ShoppingPlace(
            id = "1",
            name = "Marché Central Artisans",
            description = "Discover hand-crafted leather goods, vibrant textiles, and unique Cameroonian jewelry from the city's finest master craftsmen.",
            gradientColors = listOf(Color(0xFF1A1A2A), Color(0xFF0D0D1A))
        ),
    )

    val nightlifePlaces = listOf(
        NightlifePlace(
            id = "1",
            name = "The Onyx Lounge",
            subtitle = "Jazz & Craft Cocktails",
            gradientColors = listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28))
        ),
        NightlifePlace(
            id = "2",
            name = "Skyline Rooftop",
            subtitle = "Panoramic Views & DJ Sets",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
    )
}