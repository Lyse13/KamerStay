package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.data.model.DepartureGuest

object CheckInMockData {

    val arrivals = listOf(
        CheckInGuest(
            id = "1",
            name = "Elena Rodriguez",
            bookingId = "#MST-99021",
            room = "Room 402 (King Suite)",
            tag = "VIP Member",
            tagColor = Primary,
            arrivalTime = "Arriving 2:00 PM",
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "2",
            name = "Jameson Blake",
            bookingId = "#MST-99045",
            room = "Room 215 (Double Urban)",
            tag = "2 Bags",
            tagColor = Color(0xFF607D8B),
            arrivalTime = null,
            paymentPending = true,
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "3",
            name = "Sarah Chen",
            bookingId = "#MST-99088",
            room = "Room 310 (Standard Queen)",
            tag = "Repeat Guest (5 stays)",
            tagColor = Color(0xFF607D8B),
            arrivalTime = null,
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "4",
            name = "Marcus Thorne",
            bookingId = "#MST-98912",
            room = "Room 501 (Penthouse)",
            tag = null,
            tagColor = Color.Transparent,
            arrivalTime = null,
            checkedInTime = "Checked In 09:15 AM",
            isCheckedIn = true
        ),
    )

    val departures = listOf(
        DepartureGuest(
            initials = "JD",
            name = "John Doe",
            room = "Room 402 • Deluxe Suite",
            nights = "3 Nights",
            dates = "Oct 21 - Oct 24",
            balance = "\$0.00",
            balanceLabel = "PAID IN FULL",
            isPaid = true
        ),
        DepartureGuest(
            initials = "SM",
            name = "Sarah Miller",
            room = "Room 105 • Garden View",
            nights = "5 Nights",
            dates = "Oct 19 - Oct 24",
            balance = "\$425.50",
            balanceLabel = "UNPAID EXTRAS",
            isPaid = false
        ),
        DepartureGuest(
            initials = "RB",
            name = "Robert Brown",
            room = "Room 312 • Executive",
            nights = "1 Night",
            dates = "Oct 23 - Oct 24",
            balance = "\$0.00",
            balanceLabel = "PAID IN FULL",
            isPaid = true
        ),
        DepartureGuest(
            initials = "EW",
            name = "Emily White",
            room = "Room 208 • Twin Standard",
            nights = "2 Nights",
            dates = "Oct 22 - Oct 24",
            balance = "\$12.00",
            balanceLabel = "MINI BAR",
            isPaid = false
        ),
    )
}