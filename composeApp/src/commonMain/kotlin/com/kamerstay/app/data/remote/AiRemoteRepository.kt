package com.kamerstay.app.data.remote

import com.kamerstay.app.data.model.ConciergeRequest
import com.kamerstay.app.data.model.ConciergeResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AiRemoteRepository {
    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun sendMessage(request: ConciergeRequest): ConciergeResponse {
        return client.post("$baseUrl/ai/concierge") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}