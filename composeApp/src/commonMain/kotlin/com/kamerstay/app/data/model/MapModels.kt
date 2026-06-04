package com.kamerstay.app.data.model

data class MapHotel(
    val id: String,
    val name: String,
    val price: Int,
    val rating: Double,
    val distance: String,
    val location: String,
    val amenities: List<String>,
    val lat: Double,
    val lng: Double,
    val isSelected: Boolean = false
)
