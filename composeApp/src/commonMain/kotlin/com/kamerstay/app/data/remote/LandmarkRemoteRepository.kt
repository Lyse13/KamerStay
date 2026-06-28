package com.kamerstay.app.data.remote

import com.kamerstay.app.model.HotelWithDistance
import com.kamerstay.app.model.Landmark
import io.ktor.client.call.body
import io.ktor.client.request.get

class LandmarkRemoteRepository {
    private val client  = ApiClient.client
    private val baseUrl = ApiClient.BASE_URL

    suspend fun getAllLandmarks(): List<Landmark> =
        client.get("$baseUrl/landmarks").body()

    suspend fun getHotelsNearLandmark(landmarkId: String): List<HotelWithDistance> =
        client.get("$baseUrl/hotels/near-landmark/$landmarkId").body()
}