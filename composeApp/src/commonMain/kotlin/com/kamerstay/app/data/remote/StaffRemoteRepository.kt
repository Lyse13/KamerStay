package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.model.Staff
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class StaffRemoteRepository {

    private val client  = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    private fun authHeader() = HttpHeaders.Authorization to "Bearer ${UserSession.token}"

    suspend fun getStaff(hotelId: String): List<Staff> {
        return client.get("$baseUrl/staff?hotelId=$hotelId") {
            headers { append(authHeader().first, authHeader().second) }
        }.body()
    }

    suspend fun createStaff(staff: Staff): Staff {
        return client.post("$baseUrl/staff") {
            contentType(ContentType.Application.Json)
            headers { append(authHeader().first, authHeader().second) }
            setBody(staff)
        }.body()
    }

    suspend fun updateStaff(staff: Staff): Staff {
        return client.put("$baseUrl/staff/${staff.id}") {
            contentType(ContentType.Application.Json)
            headers { append(authHeader().first, authHeader().second) }
            setBody(staff)
        }.body()
    }

    suspend fun deleteStaff(staffId: String) {
        client.delete("$baseUrl/staff/$staffId") {
            headers { append(authHeader().first, authHeader().second) }
        }
    }
}