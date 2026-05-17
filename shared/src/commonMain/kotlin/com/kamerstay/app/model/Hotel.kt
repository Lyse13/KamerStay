package com.kamerstay.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val address: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val phoneNumber: String = "",
    val email: String = "",
    val imageUrls: List<String> = emptyList(),
    val amenities: List<String> = emptyList(),
    val isVerified: Boolean = false,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val pricePerNight: Double = 0.0,
    val managerId: String = "",
    val nearbyLandmarks: List<Landmark> = emptyList(),
    val totalRooms: Int = 0,
    val availableRooms: Int = 0,
    val createdAt: String = ""
)
