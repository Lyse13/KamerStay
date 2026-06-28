package com.kamerstay.app.data.remote

import com.kamerstay.app.data.model.PaymentInitRequest
import com.kamerstay.app.data.model.PaymentInitResponse
import com.kamerstay.app.data.model.PaymentStatusResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PaymentRemoteRepository {

    private val client  = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun initiatePayment(request: PaymentInitRequest): PaymentInitResponse {
        return client.post("$baseUrl/payments/initiate") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getPaymentStatus(reference: String): PaymentStatusResponse {
        return client.get("$baseUrl/payments/$reference/status").body()
    }
}