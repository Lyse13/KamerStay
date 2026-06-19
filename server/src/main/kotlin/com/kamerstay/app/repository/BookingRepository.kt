package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Booking
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class BookingRepository {

    private val bookings = DatabaseConfig.bookingsCollection

    suspend fun createBooking(booking: Booking): Booking {
        bookings.insertOne(booking)
        return booking
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
}