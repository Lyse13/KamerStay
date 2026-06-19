package com.kamerstay.app.data.remote

import com.kamerstay.app.model.Hotel
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class HotelRemoteRepository {

    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun getAllHotels(): List<Hotel> {
        return client.get("$baseUrl/hotels").body()
    }

    suspend fun getHotelsByCity(city: String): List<Hotel> {
        return client.get("$baseUrl/hotels") {
            parameter("city", city)
        }.body()
    }

    suspend fun getHotelById(id: String): Hotel {
        return client.get("$baseUrl/hotels/$id").body()
    }
}