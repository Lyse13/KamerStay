package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Hotel
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class HotelRepository {
    private val hotels = DatabaseConfig.hotelsCollection

    suspend fun getAllHotels(): List<Hotel> {
        return hotels.find().toList()
    }

    suspend fun getHotelById(id: String): Hotel? {
        return hotels.find(Filters.eq("id", id)).firstOrNull()
    }

    suspend fun getHotelsByCity(city: String): List<Hotel> {
        return hotels.find(Filters.eq("city", city)).toList()
    }

    suspend fun insertHotel(hotel: Hotel) {
        hotels.insertOne(hotel)
    }

    suspend fun getHotelByManagerId(managerId: String): Hotel? {
        return hotels.find(Filters.eq("managerId", managerId)).firstOrNull()
    }

    suspend fun countHotels(): Long {
        return hotels.countDocuments()
    }

    /**
     * Ajuste availableRooms de delta (+1 = libération, -1 = occupation).
     * Ne descend jamais en-dessous de 0 lors d'une décrémentation.
     */
    suspend fun adjustAvailableRooms(hotelId: String, delta: Int) {
        if (delta >= 0) {
            hotels.updateOne(
                Filters.eq("id", hotelId),
                Updates.inc("availableRooms", delta)
            )
        } else {
            // Décrémentation atomique : uniquement si availableRooms > 0
            hotels.updateOne(
                Filters.and(Filters.eq("id", hotelId), Filters.gt("availableRooms", 0)),
                Updates.inc("availableRooms", delta)
            )
        }
    }
}