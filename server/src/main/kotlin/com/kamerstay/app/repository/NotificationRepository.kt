package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable

@Serializable
data class FcmToken(
    val userId: String = "",
    val token: String = "",
    val updatedAt: String = ""
)

class NotificationRepository {
    private val tokens = DatabaseConfig.database.getCollection<FcmToken>("fcm_tokens")

    suspend fun upsertToken(userId: String, token: String) {
        val doc = FcmToken(
            userId    = userId,
            token     = token,
            updatedAt = java.time.Instant.now().toString()
        )
        tokens.replaceOne(
            Filters.eq("userId", userId),
            doc,
            ReplaceOptions().upsert(true)
        )
    }

    suspend fun getToken(userId: String): String? =
        tokens.find(Filters.eq("userId", userId)).firstOrNull()?.token
}
