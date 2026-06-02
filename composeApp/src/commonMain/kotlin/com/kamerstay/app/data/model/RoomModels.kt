package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.model.enums.RoomStatus

data class ManagerRoom(
    val id: String,
    val number: String,
    val type: String,
    val description: String,
    val price: Int,
    val status: RoomStatus,
    val gradientColors: List<Color>,
    val area: String = "",
    val extraInfo: String = ""
)