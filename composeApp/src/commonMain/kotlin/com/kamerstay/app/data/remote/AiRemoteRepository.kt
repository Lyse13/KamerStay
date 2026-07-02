package com.kamerstay.app.data.remote

import com.kamerstay.app.data.model.ConciergeRequest
import com.kamerstay.app.data.model.ConciergeResponse
import com.kamerstay.app.data.model.PricingRequest
import com.kamerstay.app.data.model.PricingResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.serialization.json.Json

class AiRemoteRepository {
    private val client = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun sendMessage(request: ConciergeRequest): ConciergeResponse {
        return client.post("$baseUrl/ai/concierge") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun streamMessage(request: ConciergeRequest): Flow<String> = channelFlow {
        client.preparePost("$baseUrl/ai/concierge/stream") {
            contentType(ContentType.Application.Json)
            setBody(request)
            // Timeouts étendus pour le SSE : Claude peut prendre >30s en mode tool use
            timeout {
                connectTimeoutMillis = 60_000L   // cold start Render
                requestTimeoutMillis = 180_000L  // durée max du stream complet
                socketTimeoutMillis  = 120_000L  // silence max entre deux chunks
            }
        }.execute { response ->
            if (!response.status.isSuccess()) {
                throw Exception("Serveur indisponible (${response.status.value})")
            }
            val channel = response.bodyAsChannel()
            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line() ?: break
                if (!line.startsWith("data:")) continue
                val data = line.removePrefix("data:").trim()
                if (data == "[DONE]") break
                try {
                    val text = Json.decodeFromString<String>(data)
                    if (text.isNotEmpty()) send(text)
                } catch (_: Exception) {}
            }
        }
    }

    suspend fun suggestPricing(request: PricingRequest): PricingResponse {
        return client.post("$baseUrl/ai/pricing") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}