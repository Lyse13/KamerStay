package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Landmark
import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class LandmarkRepository {

    private val landmarks = DatabaseConfig.database.getCollection<Landmark>("landmarks")

    suspend fun getAllLandmarks(): List<Landmark> =
        landmarks.find().toList()

    suspend fun getLandmarksByCity(city: String): List<Landmark> =
        landmarks.find(Filters.eq("city", city)).toList()

    suspend fun getLandmarkById(id: String): Landmark? =
        landmarks.find(Filters.eq("id", id)).firstOrNull()
}