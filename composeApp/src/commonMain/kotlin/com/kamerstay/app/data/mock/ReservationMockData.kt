package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.CalendarEntry
import com.kamerstay.app.data.model.Reservation
import com.kamerstay.app.data.model.ReservationDetail

object ReservationMockData {

    val reservationDetails = listOf(
        ReservationDetail(
            id = "1",
            guestName = "Ngono Essomba Aristide",
            guestInitials = "NE",
            reservationId = "#MS-88291",
            status = "Confirmé",
            checkIn = "Oct 12",
            checkOut = "Oct 18, 2024",
            nights = 6,
            roomName = "Suite Executive",
            roomDetails = "Chambre 402 • Lit King • Vue Wouri",
            phoneNumber = "+237 698 821 200",
            email = "a.essomba@camintelco.cm",
            membershipTier = "Elite Gold",
            specialRequests = "\"Le client fête son 10e anniversaire de mariage. Prévoir une bouteille de champagne et un mot de bienvenue manuscrit. Allergie aux fruits de mer. Heure d'arrivée souhaitée : 13h00 si possible.\"",
            requestTags = listOf("Champagne", "Anniversaire", "Allergie Fruits de Mer"),
            roomCharge = "1 260 000 FCFA",
            serviceFees = "189 300 FCFA",
            amenitiesAddOn = "72 000 FCFA",
            totalAmount = "1 521 300 FCFA",
            paymentStatus = "PAYÉ INTÉGRALEMENT",
            cardInfo = "MTN Mobile Money ••••7821"
        ),
    )

    fun getById(id: String) = reservationDetails.find { it.id == id }
        ?: reservationDetails.first()

    // ── Pour ReservationsScreen ───────────────────────────
    val reservations = listOf(
        Reservation(
            id = "1",
            guestName = "Patrick Nkouondjé",
            guestInitials = "PN",
            bookingId = "PN-90210",
            roomType = "Suite Deluxe",
            roomTag = "Suite Deluxe",
            checkIn = "Oct 12",
            checkOut = "Oct 15",
            nights = 3,
            status = "Confirmé",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
        Reservation(
            id = "2",
            guestName = "Marie-Claire Mbida",
            guestInitials = "MM",
            bookingId = "MM-44521",
            roomType = "Executive King",
            roomTag = "Executive King",
            checkIn = "Oct 14",
            checkOut = "Oct 18",
            nights = 4,
            status = "En attente",
            gradientColors = listOf(Color(0xFF2A3A1A), Color(0xFF1A280D))
        ),
        Reservation(
            id = "3",
            guestName = "Thierry Atangana",
            guestInitials = "TA",
            bookingId = "TA-11290",
            roomType = "Vue Wouri",
            roomTag = "Vue Wouri",
            checkIn = "Oct 16",
            checkOut = "Oct 17",
            nights = 1,
            status = "Confirmé",
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0A1525))
        ),
    )

    fun getReservationById(id: String) = reservations.find { it.id == id }
        ?: reservations.first()

    // ── Calendar data ──────────────────────────────────
    const val totalRooms = 8

    val calendarEntries = listOf(
        CalendarEntry("c1",  "Patrick Nkouondjé",    "PN", "Suite Deluxe",     1,  4,  10, 2024, Color(0xFF1976D2)),
        CalendarEntry("c2",  "Marie-Claire Mbida",   "MM", "Executive King",   3,  8,  10, 2024, Color(0xFF388E3C)),
        CalendarEntry("c3",  "Thierry Atangana",     "TA", "Vue Wouri",        5,  9,  10, 2024, Color(0xFF7B1FA2)),
        CalendarEntry("c4",  "Serge Manga",          "SM", "Supérieure Twin",  8,  13, 10, 2024, Color(0xFFF57C00)),
        CalendarEntry("c5",  "Aïssatou Diallo",      "AD", "Penthouse",        10, 15, 10, 2024, Color(0xFFC62828)),
        CalendarEntry("c6",  "Yves Tchouanté",       "YT", "Suite Deluxe",     12, 17, 10, 2024, Color(0xFF00838F)),
        CalendarEntry("c7",  "Solange Owoundi",      "SO", "Junior Suite",     13, 16, 10, 2024, Color(0xFF6D4C41)),
        CalendarEntry("c8",  "Kofi Asante",          "KA", "Chambre Standard", 14, 19, 10, 2024, Color(0xFF558B2F)),
        CalendarEntry("c9",  "Amara Ndiaye",         "AN", "Vue Wouri",        15, 19, 10, 2024, Color(0xFF4527A0)),
        CalendarEntry("c10", "Rodrigue Nkoa",        "RN", "Executive King",   18, 23, 10, 2024, Color(0xFF00695C)),
        CalendarEntry("c11", "Yemi Oladele",         "YO", "Suite Deluxe",     20, 25, 10, 2024, Color(0xFF1976D2)),
        CalendarEntry("c12", "Cécile Nguembock",     "CN", "Supérieure Twin",  22, 27, 10, 2024, Color(0xFFF57C00)),
        CalendarEntry("c13", "Claude Ndoumbe",       "CD", "Junior Suite",     25, 31, 10, 2024, Color(0xFF7B1FA2)),
        CalendarEntry("c14", "Sylvie Biloa",         "SB", "Penthouse",        27, 31, 10, 2024, Color(0xFFC62828)),
        CalendarEntry("c15", "Omar Bello",           "OB", "Chambre Standard", 28, 31, 10, 2024, Color(0xFF388E3C))
    )
}