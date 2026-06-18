package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.CheckInGuest
import com.kamerstay.app.data.model.DepartureGuest

object CheckInMockData {

    val arrivals = listOf(
        CheckInGuest(
            id = "1",
            name = "Mireille Bongue",
            bookingId = "#MST-99021",
            room = "Chambre 402 (Suite King)",
            tag = "Membre VIP",
            tagColor = Primary,
            arrivalTime = "Arrivée 14:00",
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "2",
            name = "François Mvolo",
            bookingId = "#MST-99045",
            room = "Chambre 215 (Double Urbaine)",
            tag = "2 Bagages",
            tagColor = Color(0xFF607D8B),
            arrivalTime = null,
            paymentPending = true,
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "3",
            name = "Astrid Ngo Bassong",
            bookingId = "#MST-99088",
            room = "Chambre 310 (Standard Queen)",
            tag = "Client Fidèle (5 séjours)",
            tagColor = Color(0xFF607D8B),
            arrivalTime = null,
            isCheckedIn = false
        ),
        CheckInGuest(
            id = "4",
            name = "Théodore Mballa",
            bookingId = "#MST-98912",
            room = "Chambre 501 (Penthouse)",
            tag = null,
            tagColor = Color.Transparent,
            arrivalTime = null,
            checkedInTime = "Arrivé à 09:15",
            isCheckedIn = true
        ),
    )

    val departures = listOf(
        DepartureGuest(
            initials = "HT",
            name = "Hervé Tchana",
            room = "Chambre 402 • Suite Deluxe",
            nights = "3 Nuits",
            dates = "21 Oct - 24 Oct",
            balance = "0 FCFA",
            balanceLabel = "PAYÉ INTÉGRALEMENT",
            isPaid = true
        ),
        DepartureGuest(
            initials = "CM",
            name = "Christine Mbeye",
            room = "Chambre 105 • Vue Jardin",
            nights = "5 Nuits",
            dates = "19 Oct - 24 Oct",
            balance = "255 300 FCFA",
            balanceLabel = "EXTRAS NON PAYÉS",
            isPaid = false
        ),
        DepartureGuest(
            initials = "SN",
            name = "Simon Ndam",
            room = "Chambre 312 • Executive",
            nights = "1 Nuit",
            dates = "23 Oct - 24 Oct",
            balance = "0 FCFA",
            balanceLabel = "PAYÉ INTÉGRALEMENT",
            isPaid = true
        ),
        DepartureGuest(
            initials = "AF",
            name = "Aurélie Fongang",
            room = "Chambre 208 • Twin Standard",
            nights = "2 Nuits",
            dates = "22 Oct - 24 Oct",
            balance = "7 200 FCFA",
            balanceLabel = "MINI-BAR",
            isPaid = false
        ),
    )
}