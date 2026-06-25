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
import kotlinx.serialization.json.*

// ── DTOs ─────────────────────────────────────────────────────────────────────

@Serializable
data class ConciergeChatMessage(val role: String, val content: String)

@Serializable
data class ConciergeRequest(
    val message: String,
    val history: List<ConciergeChatMessage> = emptyList(),
    val userName: String? = null,
    val userContext: String? = null
)

@Serializable
data class SearchCriteria(
    val city: String? = null,
    val budgetFcfa: Int? = null,
    val checkIn: String? = null,
    val checkOut: String? = null,
    val travelType: String? = null,
    val amenities: List<String> = emptyList()
)

@Serializable
data class ConciergeResponse(val message: String, val criteria: SearchCriteria? = null)

@Serializable
data class PricingRequest(
    val hotelName: String,
    val city: String,
    val currentOccupancyPercent: Int,
    val currentPricePerNight: Int,
    val roomType: String,
    val checkInDate: String? = null
)

@Serializable
data class PricingResponse(
    val suggestedPrice: Int,
    val explanation: String,
    val season: String,
    val demandLevel: String
)

// ── Anthropic Client ──────────────────────────────────────────────────────────

private val responseJson = Json { ignoreUnknownKeys = true; isLenient = true }

private val anthropicClient = HttpClient(CIO) {
    install(ContentNegotiation) { json(responseJson) }
}

private fun resolveApiKey(): String {
    val fromEnv = System.getenv("ANTHROPIC_API_KEY")
    if (!fromEnv.isNullOrBlank()) return fromEnv
    // Fallback: read from local.properties (development without Gradle run task)
    return try {
        val props = java.util.Properties()
        var dir = java.io.File("").absoluteFile
        repeat(6) {
            val candidate = java.io.File(dir, "local.properties")
            if (candidate.exists()) {
                props.load(candidate.inputStream())
                return props.getProperty("ANTHROPIC_API_KEY") ?: ""
            }
            dir = dir.parentFile ?: return ""
        }
        ""
    } catch (_: Exception) { "" }
}

private const val ANTHROPIC_URL = "https://api.anthropic.com/v1/messages"
private const val ANTHROPIC_VERSION = "2023-06-01"
private const val CLAUDE_MODEL = "claude-sonnet-4-6"

// ── System Prompts ────────────────────────────────────────────────────────────

private val CONCIERGE_SYSTEM_PROMPT = """
Tu es Kamsa, le concierge IA de KamerStay, la plateforme de réservation d'hôtels made in Cameroun.
Tu es chaleureux, professionnel, passionné par le Cameroun et expert en tourisme local.

LANGUES : Détecte la langue du voyageur et réponds en français, anglais ou pidgin camerounais. Mélange si nécessaire.

EXPERTISE CAMEROUNAISE :
Villes : Yaoundé (quartiers Bastos, Nlongkak, Mvan, Centre-ville), Douala (Akwa, Bonapriso, Bali, Deido, Bonanjo),
Bafoussam (marché central, quartier administratif), Kribi (plages, chutes Lobé, Tanga Beach),
Limbé (plage noire volcanique, jardin botanique, wildlife center), Garoua (fleuve Bénoué, parc Benoué),
Maroua (Extrême-Nord, quartier Domayo, marché artisanal), Ngaoundéré (train depuis Yaoundé, lamidat),
Bertoua (Est), Ebolowa (Sud), Kumba, Bamenda.

Calendrier camerounais :
- Fête Nationale : 1er mai (Fête du Travail) et 20 mai (Fête Nationale) → hôtels très demandés, prix +30-50%
- Rentrée scolaire : septembre → pic voyageurs business
- Vacances Noël/Nouvel An : décembre-janvier → haute saison, réservation à l'avance obligatoire
- Ramadan + Fête du Mouton (Aid) : impact important Nord Cameroun
- CAN / CHAN : réservations à l'avance, prix x2-3 dans les villes hôtes
- Festival Nguon (Bafoussam octobre), Festival Ngondo (Douala décembre)
- Saison des pluies : mars-juin et sept-novembre (selon région) → éviter certaines routes

Prix indicatifs FCFA/nuit :
- Budget : 10.000-30.000 FCFA
- Milieu de gamme : 30.000-80.000 FCFA
- Haut de gamme : 80.000-200.000+ FCFA

Expressions locales : "Na weti?", "How far?", "I don do", "chop", "mboulou", "na so", "wusai", "nyanga", "e be like say".

IMPORTANT - FORMAT DE RÉPONSE OBLIGATOIRE :
Réponds UNIQUEMENT avec du JSON valide, sans texte avant ou après, sans balises markdown.
Format exact (remplace les valeurs, mets null si non mentionné) :
{"message":"Ta réponse naturelle et chaleureuse ici","criteria":{"city":null,"budgetFcfa":null,"checkIn":null,"checkOut":null,"travelType":null,"amenities":[]}}

Règles criteria :
- city : nom de la ville camerounaise mentionnée (ex: "Douala") ou null
- budgetFcfa : montant en entier (ex: 50000) ou null
- checkIn/checkOut : format YYYY-MM-DD ou null
- travelType : "family", "business", "couple" ou "solo" ou null
- amenities : sous-liste de ["wifi","pool","restaurant","parking","ac","gym","spa","beach"] ou []
Conserve les critères déjà mentionnés dans l'historique sauf si le voyageur les modifie.
""".trimIndent()

private val PRICING_SYSTEM_PROMPT = """
Tu es un expert en revenue management hôtelier au Cameroun pour KamerStay.
Tu connais la saisonnalité camerounaise, les événements locaux et le marché hôtelier.
Réponds UNIQUEMENT en JSON valide sans texte additionnel :
{"suggestedPrice":0,"explanation":"explication courte en français","season":"basse|normale|haute|très haute","demandLevel":"low|medium|high|peak"}
""".trimIndent()

// ── Helper ────────────────────────────────────────────────────────────────────

private fun buildConciergePrompt(userContext: String?): String {
    if (userContext.isNullOrBlank()) return CONCIERGE_SYSTEM_PROMPT
    return "$CONCIERGE_SYSTEM_PROMPT\n\nCONTEXTE UTILISATEUR (réservations actuelles) :\n$userContext"
}

private suspend fun callClaude(systemPrompt: String, messages: List<ConciergeChatMessage>, apiKey: String): String {
    val body = buildJsonObject {
        put("model", CLAUDE_MODEL)
        put("max_tokens", 1024)
        put("system", systemPrompt)
        put("messages", buildJsonArray {
            messages.forEach { msg ->
                add(buildJsonObject {
                    put("role", msg.role)
                    put("content", msg.content)
                })
            }
        })
    }

    val responseBody = anthropicClient.post(ANTHROPIC_URL) {
        header("x-api-key", apiKey)
        header("anthropic-version", ANTHROPIC_VERSION)
        contentType(ContentType.Application.Json)
        setBody(body.toString())
    }.body<String>()

    // Parser manuellement
    val json = Json { ignoreUnknownKeys = true }
    val parsed = json.parseToJsonElement(responseBody).jsonObject

    return parsed["content"]
        ?.jsonArray
        ?.firstOrNull()
        ?.jsonObject
        ?.get("text")
        ?.jsonPrimitive
        ?.content ?: ""
}

private fun cleanJson(raw: String): String =
    raw.removePrefix("```json").removePrefix("```").removeSuffix("```").trim()

// ── Routes ────────────────────────────────────────────────────────────────────

fun Route.aiRoutes() {
    val apiKey = resolveApiKey()

    route("/ai") {

        post("/concierge") {
            if (apiKey.isBlank()) {
                call.respond(
                    HttpStatusCode.ServiceUnavailable,
                    ConciergeResponse("La clé API Anthropic n'est pas configurée sur le serveur.")
                )
                return@post
            }

            val request = call.receive<ConciergeRequest>()

            val messages = buildList {
                addAll(request.history)
                add(ConciergeChatMessage(role = "user", content = request.message))
            }

            try {
                val rawText = callClaude(buildConciergePrompt(request.userContext), messages, apiKey)
                val cleaned = cleanJson(rawText)

                val response = try {
                    responseJson.decodeFromString<ConciergeResponse>(cleaned)
                } catch (_: Exception) {
                    ConciergeResponse(
                        message = cleaned.ifBlank { "Désolé, je n'ai pas pu traiter votre demande. Réessayez." },
                        criteria = null
                    )
                }

                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ConciergeResponse("Service temporairement indisponible. Vérifiez votre connexion.")
                )
            }
        }

        post("/pricing") {
            if (apiKey.isBlank()) {
                call.respond(HttpStatusCode.ServiceUnavailable, mapOf("error" to "ANTHROPIC_API_KEY non configurée"))
                return@post
            }

            val req = call.receive<PricingRequest>()

            val userMsg = """
                Hôtel : ${req.hotelName} (${req.city})
                Type de chambre : ${req.roomType}
                Prix actuel : ${req.currentPricePerNight} FCFA/nuit
                Taux d'occupation : ${req.currentOccupancyPercent}%
                ${req.checkInDate?.let { "Date check-in : $it" } ?: ""}
                Suggère un prix optimal pour maximiser le revenu.
            """.trimIndent()

            try {
                val rawText = callClaude(
                    PRICING_SYSTEM_PROMPT,
                    listOf(ConciergeChatMessage("user", userMsg)),
                    apiKey
                )
                val response = try {
                    responseJson.decodeFromString<PricingResponse>(cleanJson(rawText))
                } catch (_: Exception) {
                    PricingResponse(
                        suggestedPrice = req.currentPricePerNight,
                        explanation = "Impossible de générer une suggestion pour le moment.",
                        season = "normale",
                        demandLevel = "medium"
                    )
                }
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Service indisponible"))
            }
        }
    }
}