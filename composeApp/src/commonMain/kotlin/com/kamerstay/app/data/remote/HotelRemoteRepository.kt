package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Room
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class HotelRemoteRepository {

    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun getAllHotels(): List<Hotel> {
        return client.get("$baseUrl/hotels").body()
    }

    suspend fun getMyHotel(): Hotel? {
        val response = client.get("$baseUrl/hotels/my") {
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
        }
        return if (response.status.value == 200) response.body() else null
    }

    suspend fun getHotelsByCity(city: String): List<Hotel> {
        return client.get("$baseUrl/hotels") {
            parameter("city", city)
        }.body()
    }

    suspend fun getHotelById(id: String): Hotel {
        return client.get("$baseUrl/hotels/$id").body()
    }

    suspend fun getRoomsForHotel(hotelId: String): List<Room> {
        return client.get("$baseUrl/hotels/$hotelId/rooms").body()
    }

    suspend fun createHotel(hotel: Hotel): Hotel {
        return client.post("$baseUrl/hotels") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
            setBody(hotel)
        }.body()
    }

    suspend fun createRoomForHotel(hotelId: String, room: com.kamerstay.app.model.Room): com.kamerstay.app.model.Room {
        return client.post("$baseUrl/hotels/$hotelId/rooms") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
            setBody(room)
        }.body()
    }

    suspend fun updateRoom(hotelId: String, room: com.kamerstay.app.model.Room): com.kamerstay.app.model.Room {
        return client.put("$baseUrl/hotels/$hotelId/rooms/${room.id}") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
            setBody(room)
        }.body()
    }

    suspend fun deleteRoom(hotelId: String, roomId: String) {
        client.delete("$baseUrl/hotels/$hotelId/rooms/$roomId") {
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
        }
    }
}