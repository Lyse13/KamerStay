package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.*

object GuideMockData {

    val landmarks = listOf(
        Landmark(
            id = "1",
            name = "Jardin Botanique de Limbé",
            description = "Fondé en 1892, ce sanctuaire luxuriant de 250 hectares abrite plus de 1 500 espèces végétales tropicales. Un havre de paix incontournable au pied du Mont Cameroun.",
            rating = 4.9,
            reviewCount = "1.2k",
            price = "9 000 FCFA",
            gradientColors = listOf(Color(0xFF1A3A1A), Color(0xFF0D280D)),
            imageUrl = "https://images.unsplash.com/photo-1585320806297-9794b3e4aaae?w=800&fit=crop&auto=format"
        ),
        Landmark(
            id = "2",
            name = "Monument de la Réunification",
            description = "Symbole puissant de l'unité nationale, ce monument emblématique de Yaoundé commémore la réunification des deux Cameroun en 1961. Entouré de jardins paysagers.",
            rating = 4.7,
            reviewCount = "890",
            price = "Gratuit",
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF1A1208)),
            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&fit=crop&auto=format"
        ),
        Landmark(
            id = "3",
            name = "Chutes de la Lobé",
            description = "L'une des rares chutes d'eau au monde qui se jette directement dans l'océan. Ce site naturel exceptionnel près de Kribi offre un panorama à couper le souffle.",
            rating = 4.8,
            reviewCount = "654",
            price = "5 000 FCFA",
            gradientColors = listOf(Color(0xFF0D3A4A), Color(0xFF062030)),
            imageUrl = "https://images.unsplash.com/photo-1505118380757-91f5f5632de0?w=800&fit=crop&auto=format"
        ),
    )

    val foodPlaces = listOf(
        FoodPlace(
            id = "1",
            name = "Le Panoramique",
            location = "Akwa, Douala",
            tag = "GASTRONOMIQUE",
            tagColor = Color(0xFF003761),
            gradientColors = listOf(Color(0xFF1A1A3A), Color(0xFF0D0D28)),
            imageUrl = "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&fit=crop&auto=format"
        ),
        FoodPlace(
            id = "2",
            name = "Terroir Camerounais",
            location = "Bastos, Yaoundé",
            tag = "CUISINE LOCALE",
            tagColor = Color(0xFF8B4500),
            gradientColors = listOf(Color(0xFF3A2A1A), Color(0xFF28180D)),
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&fit=crop&auto=format"
        ),
        FoodPlace(
            id = "3",
            name = "Marché Central Ndokoti",
            location = "Akwa, Douala",
            tag = "STREET FOOD",
            tagColor = Color(0xFF6B3A00),
            gradientColors = listOf(Color(0xFF3A1A0D), Color(0xFF1A0A06)),
            imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&fit=crop&auto=format"
        ),
    )

    val shoppingPlaces = listOf(
        ShoppingPlace(
            id = "1",
            name = "Marché des Artisans",
            description = "Découvrez des maroquineries artisanales, des tissus wax colorés et des bijoux camerounais uniques créés par les meilleurs artisans de la ville.",
            gradientColors = listOf(Color(0xFF1A1A2A), Color(0xFF0D0D1A)),
            imageUrl = "https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=800&fit=crop&auto=format"
        ),
        ShoppingPlace(
            id = "2",
            name = "Galerie Wouri Mall",
            description = "Centre commercial moderne au cœur de Douala regroupant les plus grandes enseignes internationales et locales sur 3 niveaux.",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28)),
            imageUrl = "https://images.unsplash.com/photo-1555529902-5261145633bf?w=800&fit=crop&auto=format"
        ),
    )

    val nightlifePlaces = listOf(
        NightlifePlace(
            id = "1",
            name = "The Onyx Lounge",
            subtitle = "Jazz & Cocktails Artisanaux — Akwa, Douala",
            gradientColors = listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28)),
            imageUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800&fit=crop&auto=format"
        ),
        NightlifePlace(
            id = "2",
            name = "Skyline Rooftop Bar",
            subtitle = "Vue Panoramique & DJ Sets — Bonapriso, Douala",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28)),
            imageUrl = "https://images.unsplash.com/photo-1543007630-9710e4a00a20?w=800&fit=crop&auto=format"
        ),
        NightlifePlace(
            id = "3",
            name = "Ambassadeurs Club",
            subtitle = "Musique Afrobeat & Lounge — Bastos, Yaoundé",
            gradientColors = listOf(Color(0xFF3A1A1A), Color(0xFF280D0D)),
            imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&fit=crop&auto=format"
        ),
    )
}