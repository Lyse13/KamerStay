package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.data.model.ReservationDetail

object ReservationMockData {

    val reservationDetails = listOf(
        ReservationDetail(
            id = "1",
            guestName = "Alexander Sterling",
            guestInitials = "JD",
            reservationId = "#MS-88291",
            status = "Confirmed",
            checkIn = "Oct 12",
            checkOut = "Oct 18, 2023",
            nights = 6,
            roomName = "Executive Ocean Suite",
            roomDetails = "Room 402 • King Bed • Sea View",
            phoneNumber = "+1(555) 982-1200",
            email = "a.sterling@corporate.com",
            membershipTier = "Elite Gold",
            specialRequests = "\"Guest is celebrating a 10th anniversary. Please arrange for a bottle of chilled Champagne and a hand-written welcome note. Allergic to shellfish. Preferred check-in time: 1:00 PM if possible.\"",
            requestTags = listOf("Champagne", "Anniversary", "Shellfish Allergy"),
            roomCharge = "\$2,100.00",
            serviceFees = "\$315.50",
            amenitiesAddOn = "\$120.00",
            totalAmount = "\$2,535.50",
            paymentStatus = "PAID IN FULL",
            cardInfo = "Visa ending in •••• 4492"
        ),
    )

    fun getById(id: String) = reservationDetails.find { it.id == id }
        ?: reservationDetails.first()

    // ── Pour ReservationsScreen ───────────────────────────
    val reservations = listOf(
        Reservation(
            id = "1",
            guestName = "Alexander Wright",
            guestInitials = "AW",
            bookingId = "AW-90210",
            roomType = "Deluxe Suite",
            roomTag = "Deluxe Suite",
            checkIn = "Oct 12",
            checkOut = "Oct 15",
            nights = 3,
            status = "Confirmed",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
        Reservation(
            id = "2",
            guestName = "Elena Rodriguez",
            guestInitials = "ER",
            bookingId = "ER-44521",
            roomType = "Executive King",
            roomTag = "Executive King",
            checkIn = "Oct 14",
            checkOut = "Oct 18",
            nights = 4,
            status = "Pending",
            gradientColors = listOf(Color(0xFF2A3A1A), Color(0xFF1A280D))
        ),
        Reservation(
            id = "3",
            guestName = "Jordan Smith",
            guestInitials = "JS",
            bookingId = "JS-11290",
            roomType = "Ocean View",
            roomTag = "Ocean View",
            checkIn = "Oct 16",
            checkOut = "Oct 17",
            nights = 1,
            status = "Confirmed",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0A1525))
        ),
    )

    fun getReservationById(id: String) = reservations.find { it.id == id }
        ?: reservations.first()
}
