package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RoomStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED,
    CLEANING
}