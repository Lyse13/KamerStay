package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.model.dto.AuthResponse
import com.kamerstay.app.model.dto.LoginRequest
import com.kamerstay.app.model.dto.RegisterRequest
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val demoCode: String = ""
)

@Serializable
data class VerifyCodeResponse(
    val valid: Boolean,
    val resetToken: String = "",
    val message: String = ""
)

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

    suspend fun forgotPassword(email: String): ForgotPasswordResponse {
        return client.post("$baseUrl/auth/forgot-password") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email))
        }.body()
    }

    suspend fun verifyCode(email: String, code: String): VerifyCodeResponse {
        return client.post("$baseUrl/auth/verify-code") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email, "code" to code))
        }.body()
    }

    suspend fun resetPassword(resetToken: String, newPassword: String) {
        client.post("$baseUrl/auth/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("resetToken" to resetToken, "newPassword" to newPassword))
        }
    }

    suspend fun updateProfile(fullName: String, phoneNumber: String) {
        client.put("$baseUrl/auth/me") {
            contentType(ContentType.Application.Json)
            headers { append(HttpHeaders.Authorization, "Bearer ${UserSession.token}") }
            setBody(mapOf("fullName" to fullName, "phoneNumber" to phoneNumber))
        }
    }
}