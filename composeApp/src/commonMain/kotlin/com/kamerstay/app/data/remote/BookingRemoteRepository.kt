package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.model.Booking
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class BookingRemoteRepository {

    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun createBooking(booking: Booking): Booking {
        return client.post("$baseUrl/bookings") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
            setBody(booking)
        }.body()
    }

    suspend fun getMyBookings(): List<Booking> {
        return client.get("$baseUrl/bookings/my") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
        }.body()
    }

    suspend fun getAllBookings(): List<Booking> {
        return client.get("$baseUrl/bookings/all") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
        }.body()
    }

    suspend fun getHotelBookings(hotelId: String): List<Booking> {
        return client.get("$baseUrl/bookings/hotel/$hotelId") {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
        }.body()
    }

    suspend fun updateBookingStatus(bookingId: String, status: String) {
        client.put("$baseUrl/bookings/$bookingId/status") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
            setBody(mapOf("status" to status))
        }
    }
}