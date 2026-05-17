package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.core.theme.DeepEmerald
import com.kamerstay.app.core.theme.ErrorColor
import com.kamerstay.app.core.theme.OnSurfaceVariant
import com.kamerstay.app.core.theme.WarmAmber

data class MockReservation(
    val id: String,
    val guestName: String,
    val roomInfo: String,
    val dates: String,
    val status: String,
    val avatarColor: Color,
    val initials: String
)

val mockReservations = listOf(
    MockReservation(
        id = "1",
        guestName = "Jean-Paul N.",
        roomInfo = "Room 101 — Executive Suite",
        dates = "Oct 24 - Oct 28",
        status = "Confirmed",
        avatarColor = DeepEmerald,
        initials = "JP"
    ),
    MockReservation(
        id = "2",
        guestName = "Amina Bello",
        roomInfo = "Room 304 — Deluxe Garden View",
        dates = "Nov 02 - Nov 05",
        status = "Pending",
        avatarColor = WarmAmber,
        initials = "AB"
    ),
    MockReservation(
        id = "3",
        guestName = "Samuel Eto'o",
        roomInfo = "Room 505 — Presidential Suite",
        dates = "Oct 18 - Oct 20",
        status = "Completed",
        avatarColor = OnSurfaceVariant,
        initials = "SE"
    ),
    MockReservation(
        id = "4",
        guestName = "Marie Claire",
        roomInfo = "Room 212 — Standard Twin",
        dates = "Oct 25 - Oct 26",
        status = "Cancelled",
        avatarColor = ErrorColor.copy(alpha = 0.7f),
        initials = "MC"
    ),
    MockReservation(
        id = "5",
        guestName = "Christian M.",
        roomInfo = "Room 108 — Executive Suite",
        dates = "Oct 29 - Nov 02",
        status = "Confirmed",
        avatarColor = DeepEmerald,
        initials = "CM"
    ),
)
