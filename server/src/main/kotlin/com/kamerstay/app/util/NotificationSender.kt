package com.kamerstay.app.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

object NotificationSender {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) { json(json) }
    }

    private fun resolveServerKey(): String =
        System.getenv("FIREBASE_SERVER_KEY") ?: run {
            try {
                val props = java.util.Properties()
                var dir = java.io.File("").absoluteFile
                repeat(6) {
                    val f = java.io.File(dir, "local.properties")
                    if (f.exists()) { props.load(f.inputStream()); return props.getProperty("FIREBASE_SERVER_KEY") ?: "" }
                    dir = dir.parentFile ?: return ""
                }
                ""
            } catch (_: Exception) { "" }
        }

    suspend fun send(
        fcmToken: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Boolean {
        val key = resolveServerKey()
        if (key.isBlank()) {
            println("[KamerStay] FIREBASE_SERVER_KEY non configurée — notification non envoyée")
            return false
        }
        return try {
            val payload = buildJsonObject {
                put("to", fcmToken)
                put("notification", buildJsonObject {
                    put("title", title)
                    put("body", body)
                    put("sound", "default")
                })
                if (data.isNotEmpty()) {
                    put("data", buildJsonObject { data.forEach { (k, v) -> put(k, v) } })
                }
            }
            val response = httpClient.post("https://fcm.googleapis.com/fcm/send") {
                header("Authorization", "key=$key")
                contentType(ContentType.Application.Json)
                setBody(payload.toString())
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            println("[KamerStay] Erreur FCM : ${e.message}")
            false
        }
    }
}
