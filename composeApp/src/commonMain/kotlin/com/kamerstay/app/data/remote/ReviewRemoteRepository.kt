package com.kamerstay.app.data.remote

import com.kamerstay.app.data.state.UserSession
import com.kamerstay.app.model.Review
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ReviewRemoteRepository {

    private val client  = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun getReviewsForHotel(hotelId: String): List<Review> {
        return client.get("$baseUrl/reviews/hotel/$hotelId").body()
    }

    suspend fun submitReview(review: Review): Review {
        return client.post("$baseUrl/reviews") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${UserSession.token}")
            }
            setBody(review)
        }.body()
    }
}