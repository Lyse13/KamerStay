package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class NotificationRemoteRepository {
    private val client  = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun registerFcmToken(token: String) {
        client.put("$baseUrl/auth/fcm-token") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
            setBody(mapOf("token" to token))
        }
    }
}
