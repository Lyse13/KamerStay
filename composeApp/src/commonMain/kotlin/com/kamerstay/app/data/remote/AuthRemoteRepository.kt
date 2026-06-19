package com.kamerstay.app.data.remote

import com.kamerstay.app.model.dto.AuthResponse
import com.kamerstay.app.model.dto.LoginRequest
import com.kamerstay.app.model.dto.RegisterRequest
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRemoteRepository {

    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun register(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        role: String
    ): AuthResponse {
        return client.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(fullName, email, phoneNumber, password, role))
        }.body()
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
    }
}