package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Booking
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class BookingRepository {

    private val bookings = DatabaseConfig.bookingsCollection

    // Statuts qui bloquent la chambre pour de nouvelles réservations
    private val blockingStatuses = listOf("PENDING", "CONFIRMED", "CHECKED_IN")

    suspend fun createBooking(booking: Booking): Booking {
        bookings.insertOne(booking)
        return booking
    }

    suspend fun getAllBookings(): List<Booking> {
        return bookings.find().toList()
    }

    suspend fun getBookingsByTraveler(travelerId: String): List<Booking> {
        return bookings.find(Filters.eq("travelerId", travelerId)).toList()
    }

    suspend fun getBookingsByHotel(hotelId: String): List<Booking> {
        return bookings.find(Filters.eq("hotelId", hotelId)).toList()
    }

    suspend fun getBookingById(id: String): Booking? {
        return bookings.find(Filters.eq("id", id)).firstOrNull()
    }

    suspend fun updateBookingStatus(id: String, status: String): Boolean {
        val result = bookings.updateOne(
            Filters.eq("id", id),
            com.mongodb.client.model.Updates.set("bookingStatus", status)
        )
        return result.modifiedCount > 0
    }

    /**
     * Détecte un chevauchement de dates pour une chambre donnée.
     * Deux périodes [A, B[ et [C, D[ se chevauchent si A < D ET C < B.
     * Seuls les statuts PENDING, CONFIRMED, CHECKED_IN sont bloquants.
     */
    suspend fun hasOverlappingBooking(roomId: String, checkIn: String, checkOut: String): Boolean {
        if (roomId.isBlank() || checkIn.isBlank() || checkOut.isBlank()) {
            println("[DEBUG-OVERLAP] SKIPPED — roomId='$roomId' checkIn='$checkIn' checkOut='$checkOut'")
            return false
        }
        println("[DEBUG-OVERLAP] Checking roomId='$roomId' checkIn='$checkIn' checkOut='$checkOut'")
        val filter = Filters.and(
            Filters.eq("roomId", roomId),
            Filters.`in`("bookingStatus", blockingStatuses),
            Filters.lt("checkInDate", checkOut),
            Filters.gt("checkOutDate", checkIn)
        )
        val conflict = bookings.find(filter).firstOrNull()
        println("[DEBUG-OVERLAP] conflict=${conflict != null}, conflictId=${conflict?.id}, conflictStatus=${conflict?.bookingStatus}")
        return conflict != null
    }

    /**
     * Indique si la réservation actuelle a un statut bloquant (pour savoir si
     * libérer la chambre lors d'une annulation/checkout).
     */
    fun isBlockingStatus(status: String): Boolean = status.uppercase() in blockingStatuses
}