package com.kamerstay.app.core.utils

object Constants {
    const val BASE_URL = "http://10.0.2.2:8080"  // Android emulator → localhost
    const val DEPOSIT_PERCENT = 0.30              // 30% d'acompte
    const val TOKEN_KEY = "auth_token"
    const val USER_KEY = "user_data"

    object Cities {
        val CAMEROON_CITIES = listOf(
            "Yaoundé", "Douala", "Bamenda", "Bafoussam",
            "Garoua", "Maroua", "Ngaoundéré", "Bertoua",
            "Ebolowa", "Kribi", "Limbé", "Buea"
        )
    }

    object Amenities {
        val ALL = listOf(
            "WiFi", "Parking", "Piscine", "Restaurant",
            "Climatisation", "Salle de sport", "Bar",
            "Blanchisserie", "Room Service", "Sécurité 24h"
        )
    }

    object RoomFeatures {
        val ALL = listOf(
            "TV écran plat", "Climatisation", "Mini-bar",
            "Coffre-fort", "Balcon", "Vue sur jardin",
            "Baignoire", "Douche", "Bureau de travail"
        )
    }
}