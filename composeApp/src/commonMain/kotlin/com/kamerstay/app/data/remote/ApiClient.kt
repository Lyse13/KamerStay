package com.kamerstay.app.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {

    // En développement sur émulateur Android : utilise 10.0.2.2 (qui pointe vers localhost de ta machine)
    // Sur appareil physique connecté au même Wi-Fi : utilise l'IP locale de ta machine (ex: 192.168.1.X)
    // Sur desktop/JVM : utilise localhost directement
    const val BASE_URL = "http://10.0.2.2:8080"

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }
}