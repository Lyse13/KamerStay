package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.ManagerRoom
import com.kamerstay.app.model.enums.RoomStatus

object RoomsMockData {

    val rooms = listOf(
        ManagerRoom(
            id = "1", number = "101",
            type = "Suite", description = "King Bed • Ocean View • 45m²",
            price = 240, status = RoomStatus.AVAILABLE,
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
        ManagerRoom(
            id = "2", number = "204",
            type = "Deluxe", description = "Queen Bed • City View • 32m²",
            price = 185, status = RoomStatus.OCCUPIED,
            gradientColors = listOf(Color(0xFF1A3A2E), Color(0xFF0D2218)),
            extraInfo = "Check-out: Tomorrow, 11:00 AM"
        ),
        ManagerRoom(
            id = "3", number = "305",
            type = "Suite", description = "King Bed • Terrace • 50m²",
            price = 290, status = RoomStatus.CLEANING,
            gradientColors = listOf(Color(0xFF2A2A3A), Color(0xFF1A1A28)),
            extraInfo = "Est. completion: 45 mins"
        ),
        ManagerRoom(
            id = "4", number = "501",
            type = "Penthouse", description = "King Bed • Spa Bath • 110m²",
            price = 550, status = RoomStatus.AVAILABLE,
            gradientColors = listOf(Color(0xFF1A2E3A), Color(0xFF0D1E28))
        ),
        ManagerRoom(
            id = "5", number = "102",
            type = "Standard", description = "Double Queen • Pool View • 38m²",
            price = 210, status = RoomStatus.AVAILABLE,
            gradientColors = listOf(Color(0xFF1A3A3A), Color(0xFF0D2828))
        ),
    )
}