package com.kamerstay.app.model

import com.kamerstay.app.model.enums.RoomStatus
import com.kamerstay.app.model.enums.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String = "",
    val hotelId: String = "",
    val roomNumber: String = "",
    val type: RoomType = RoomType.SINGLE,
    val status: RoomStatus = RoomStatus.AVAILABLE,
    val pricePerNight: Double = 0.0,
    val capacity: Int = 1,
    val description: String = "",
    val imageUrls: List<String> = emptyList(),
    val features: List<String> = emptyList(),
    val floor: Int = 1,
    val size: Double = 0.0
)
