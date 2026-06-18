package com.kamerstay.app.data.model

enum class LandmarkType {
    MARKET, STADIUM, MAIRIE, UNIVERSITY, HOSPITAL, AIRPORT, MONUMENT, QUARTIER, PORT, GARE
}

fun LandmarkType.emoji() = when (this) {
    LandmarkType.MARKET     -> "🏪"
    LandmarkType.STADIUM    -> "🏟️"
    LandmarkType.MAIRIE     -> "🏛️"
    LandmarkType.UNIVERSITY -> "🎓"
    LandmarkType.HOSPITAL   -> "🏥"
    LandmarkType.AIRPORT    -> "✈️"
    LandmarkType.MONUMENT   -> "🗿"
    LandmarkType.QUARTIER   -> "📍"
    LandmarkType.PORT       -> "⚓"
    LandmarkType.GARE       -> "🚂"
}

data class CameroonLandmark(
    val id: String,
    val name: String,
    val displayName: String, // "à côté du marché Etoudi"
    val lat: Double,
    val lng: Double,
    val type: LandmarkType,
    val city: String
)

data class MapHotel(
    val id: String,
    val name: String,
    val priceXaf: Int,
    val rating: Double,
    val distance: String,
    val location: String,
    val amenities: List<String>,
    val lat: Double,
    val lng: Double,
    val city: String = "douala",
    val imageUrl: String = "",
    val availableRooms: Int = 5,
    val nearbyLandmarks: List<String> = emptyList()
)

data class MapCity(
    val key: String,
    val displayName: String,
    val lat: Double,
    val lng: Double,
    val zoom: Double = 13.5
)