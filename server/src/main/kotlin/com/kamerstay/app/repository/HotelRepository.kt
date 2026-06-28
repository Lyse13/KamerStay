package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Hotel
import com.mongodb.client.model.Filters
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
}