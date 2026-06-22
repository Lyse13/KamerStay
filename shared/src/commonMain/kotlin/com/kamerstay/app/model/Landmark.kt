package com.kamerstay.app.model

import com.kamerstay.app.model.enums.LandmarkType
import kotlinx.serialization.Serializable

@Serializable
data class Landmark(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val icon: String = ""
)
@Serializable
data class HotelWithDistance(
    val hotel: Hotel = Hotel(),
    val distanceKm: Double = 0.0,
    val estimatedMinutes: Int = 0
)
