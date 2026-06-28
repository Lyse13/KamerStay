package com.kamerstay.app.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ── DTOs ─────────────────────────────────────────────────────────────────────

@Serializable
data class PaymentInitRequest(
    val amount: Double,
    val phone: String,       // format: 6XXXXXXXX (9 chiffres, sans indicatif)
    val operator: String,    // "MTN" ou "ORANGE"
    val description: String
)

@Serializable
data class PaymentInitResponse(
    val reference: String,
    val status: String,
    val operator: String,
    val message: String = ""
)

@Serializable
data class PaymentStatusResponse(
    val reference: String,
    val status: String,      // "SUCCESSFUL" | "FAILED" | "PENDING"
    val operator: String = "",
    val amount: String = "",
    val currency: String = "XAF"
)

// ── Campay API client ─────────────────────────────────────────────────────────

private val campayJson = Json { ignoreUnknownKeys = true; isLenient = true }

private val campayClient = HttpClient(CIO) {
    install(ContentNegotiation) { json(campayJson) }
}

private fun campayBaseUrl(): String {
    val isProduction = System.getenv("CAMPAY_PRODUCTION")?.lowercase() == "true"
    return if (isProduction) "https://campay.net/api" else "https://demo.campay.net/api"
}

private fun isSimulationMode(): Boolean =
    System.getenv("CAMPAY_SIMULATION")?.lowercase() == "true"

private fun resolveCampayKey(): String =
    System.getenv("CAMPAY_API_KEY") ?: run {
        // Fallback local.properties pour le dev
        try {
            val props = java.util.Properties()
            var dir = java.io.File("").absoluteFile
            repeat(6) {
                val f = java.io.File(dir, "local.properties")
                if (f.exists()) { props.load(f.inputStream()); return props.getProperty("CAMPAY_API_KEY") ?: "" }
                dir = dir.parentFile ?: return ""
            }
            ""
        } catch (_: Exception) { "" }
    }

// Normalise le numéro camerounais : "6XXXXXXXX" → "237XXXXXXXXX"
private fun normalizePhone(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    return when {
        digits.startsWith("237") -> digits
        digits.startsWith("6") && digits.length == 9 -> "237$digits"
        else -> digits
    }
}

// ── Routes ────────────────────────────────────────────────────────────────────

fun Route.paymentRoutes() {

    val apiKey = resolveCampayKey()
    val baseUrl = campayBaseUrl()

    route("/payments") {

        // Initier un paiement Mobile Money via Campay
        post("/initiate") {

            val req = call.receive<PaymentInitRequest>()
            if (isSimulationMode()) {
                val fakeRef = "SIM-${System.currentTimeMillis()}"
                call.respond(
                    HttpStatusCode.OK,
                    PaymentInitResponse(
                        reference = fakeRef,
                        status    = "PENDING",
                        operator  = req.operator
                    )
                )
                return@post
            }

            if (apiKey.isBlank()) {
                call.respond(
                    HttpStatusCode.ServiceUnavailable,
                    PaymentInitResponse("", "ERROR", "", "Clé Campay non configurée sur le serveur")
                )
                return@post
            }

            val phone = normalizePhone(req.phone)

            try {
                val response = campayClient.post("$baseUrl/collect/") {
                    header("Authorization", "Token $apiKey")
                    contentType(ContentType.Application.Json)
                    setBody(
                        mapOf(
                            "amount"      to req.amount.toInt().toString(),
                            "currency"    to "XAF",
                            "from"        to phone,
                            "description" to req.description
                        )
                    )
                }

                val body = campayJson.parseToJsonElement(response.body<String>())
                    .let { it as? kotlinx.serialization.json.JsonObject }
                    ?: return@post call.respond(
                        HttpStatusCode.InternalServerError,
                        PaymentInitResponse("", "ERROR", "", "Réponse Campay invalide")
                    )

                val reference = body["reference"]?.let {
                    (it as? kotlinx.serialization.json.JsonPrimitive)?.content
                } ?: ""

                if (reference.isBlank()) {
                    val errorMsg = body["detail"]?.let {
                        (it as? kotlinx.serialization.json.JsonPrimitive)?.content
                    } ?: "Numéro invalide ou opérateur non reconnu"
                    call.respond(
                        HttpStatusCode.BadRequest,
                        PaymentInitResponse("", "FAILED", "", errorMsg)
                    )
                    return@post
                }

                call.respond(
                    HttpStatusCode.OK,
                    PaymentInitResponse(
                        reference = reference,
                        status    = "PENDING",
                        operator  = req.operator
                    )
                )

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    PaymentInitResponse("", "ERROR", "", "Service de paiement indisponible")
                )
            }
        }

        // Vérifier le statut d'une transaction
        get("/{reference}/status") {
            val reference = call.parameters["reference"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "reference manquante")
                )

            if (isSimulationMode() || reference.startsWith("SIM-")) {
                call.respond(
                    HttpStatusCode.OK,
                    PaymentStatusResponse(
                        reference = reference,
                        status    = "SUCCESSFUL",
                        operator  = "MTN",
                        amount    = ""
                    )
                )
                return@get
            }

            if (apiKey.isBlank()) {
                call.respond(
                    HttpStatusCode.ServiceUnavailable,
                    PaymentStatusResponse(reference, "ERROR")
                )
                return@get
            }

            try {
                val response = campayClient.get("$baseUrl/transaction/$reference/") {
                    header("Authorization", "Token $apiKey")
                }

                val body = campayJson.parseToJsonElement(response.body<String>())
                    .let { it as? kotlinx.serialization.json.JsonObject }

                val status   = body?.get("status")?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } ?: "FAILED"
                val operator = body?.get("operator")?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } ?: ""
                val amount   = body?.get("amount")?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.content } ?: ""

                call.respond(
                    HttpStatusCode.OK,
                    PaymentStatusResponse(
                        reference = reference,
                        status    = status,
                        operator  = operator,
                        amount    = amount
                    )
                )

            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    PaymentStatusResponse(reference, "FAILED")
                )
            }
        }
    }
}
