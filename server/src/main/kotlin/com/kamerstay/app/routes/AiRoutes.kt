package com.kamerstay.app.routes

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
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
    val userContext: String? = null,
    val hotelsContext: String? = null
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

LANGUE — RÈGLE ABSOLUE :
1. Analyse la langue utilisée par le voyageur dans son dernier message.
2. Réponds ENTIÈREMENT dans cette même langue : si français → tout en français ; si anglais → tout en anglais ; si pidgin camerounais → tout en pidgin.
3. Ne mélange JAMAIS deux langues dans le même message. Chaque paragraphe, chaque phrase, chaque mot doit être dans la langue choisie.
4. Tu peux glisser UNE SEULE expression locale emblématique (ex : "na so !", "how far?") uniquement si elle est naturellement intégrée et immédiatement compréhensible dans le contexte, mais le reste du message reste dans la langue détectée.
5. Si la langue du voyageur change au fil de la conversation, adapte-toi à sa dernière entrée.

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

Expressions locales (à utiliser avec parcimonie, max une par réponse) : "Na weti?", "How far?", "I don do", "na so", "wusai", "nyanga".

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

// Prompt pour le streaming — même expertise, réponse en texte naturel (pas JSON)
private val CONCIERGE_STREAMING_PROMPT = """
Tu es Kamsa, le concierge IA de KamerStay, la plateforme de réservation d'hôtels made in Cameroun.
Tu es chaleureux, professionnel, passionné par le Cameroun et expert en tourisme local.

LANGUE — RÈGLE ABSOLUE :
1. Analyse la langue utilisée par le voyageur dans son dernier message.
2. Réponds ENTIÈREMENT dans cette même langue : si français → tout en français ; si anglais → tout en anglais ; si pidgin → tout en pidgin.
3. Ne mélange JAMAIS deux langues dans le même message.
4. Tu peux glisser UNE SEULE expression locale emblématique uniquement si elle est naturellement intégrée.
5. Si la langue change au fil de la conversation, adapte-toi à la dernière entrée.

EXPERTISE CAMEROUNAISE :
Villes : Yaoundé (Bastos, Nlongkak, Mvan, Centre-ville), Douala (Akwa, Bonapriso, Bali, Deido, Bonanjo),
Bafoussam, Kribi (plages, chutes Lobé), Limbé (plage volcanique, jardin botanique),
Garoua (Bénoué), Maroua (Extrême-Nord), Ngaoundéré, Bertoua, Ebolowa, Kumba, Bamenda.

Calendrier : 20 mai (Fête Nationale), décembre-janvier (haute saison), CAN/CHAN (prix x2-3).
Saison des pluies : mars-juin et sept-novembre selon la région.

Prix indicatifs FCFA/nuit : Budget 10k-30k · Milieu 30k-80k · Haut de gamme 80k-200k+

RÉPONSE : Réponds directement de façon naturelle et conversationnelle.
Ne génère AUCUN JSON, AUCUN format technique. Ton texte est affiché tel quel à l'utilisateur.
""".trimIndent()

private val PRICING_SYSTEM_PROMPT = """
Tu es un expert en revenue management hôtelier au Cameroun pour KamerStay.
Tu connais la saisonnalité camerounaise, les événements locaux et le marché hôtelier.
Réponds UNIQUEMENT en JSON valide sans texte additionnel :
{"suggestedPrice":0,"explanation":"explication courte en français","season":"basse|normale|haute|très haute","demandLevel":"low|medium|high|peak"}
""".trimIndent()

// ── Helper ────────────────────────────────────────────────────────────────────

private fun buildConciergePrompt(request: ConciergeRequest): String = buildString {
    append(CONCIERGE_SYSTEM_PROMPT)
    if (!request.hotelsContext.isNullOrBlank()) {
        append("\n\n")
        append(request.hotelsContext)
    }
    if (!request.userContext.isNullOrBlank()) {
        append("\n\nCONTEXTE UTILISATEUR (réservations actuelles) :\n")
        append(request.userContext)
    }
}

private suspend fun callClaude(
    systemPrompt: String,
    messages: List<ConciergeChatMessage>,
    apiKey: String,
    temperature: Double = 0.7
): String {
    val body = buildJsonObject {
        put("model", CLAUDE_MODEL)
        put("max_tokens", 2048)
        put("temperature", temperature)
        // system en array avec cache_control : Anthropic met le prompt en cache 5 min
        // → latence -1 à 2s, coût -90% sur les tokens du system prompt
        put("system", buildJsonArray {
            add(buildJsonObject {
                put("type", "text")
                put("text", systemPrompt)
                put("cache_control", buildJsonObject { put("type", "ephemeral") })
            })
        })
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

// ── Tool Use ─────────────────────────────────────────────────────────────────

private data class ToolCallAcc(
    val id: String,
    val name: String,
    val inputJson: StringBuilder = StringBuilder()
)

private val CONCIERGE_TOOLS = buildJsonArray {
    add(buildJsonObject {
        put("name", "search_hotels")
        put("description", "Cherche les hôtels disponibles sur KamerStay. Utilise cet outil quand l'utilisateur demande des hôtels dans une ville, avec un budget ou des équipements précis.")
        put("input_schema", buildJsonObject {
            put("type", "object")
            put("properties", buildJsonObject {
                put("city", buildJsonObject {
                    put("type", "string")
                    put("description", "Ville camerounaise : Douala, Yaoundé, Kribi, Limbé, Bafoussam, Garoua, Maroua…")
                })
                put("max_budget_fcfa", buildJsonObject {
                    put("type", "integer")
                    put("description", "Budget maximum par nuit en FCFA. Ex: 50000")
                })
                put("amenities", buildJsonObject {
                    put("type", "array")
                    put("items", buildJsonObject { put("type", "string") })
                    put("description", "Équipements : wifi, pool, restaurant, parking, ac, gym, spa, beach")
                })
            })
            put("required", buildJsonArray { add("city") })
        })
    })
    add(buildJsonObject {
        put("name", "get_hotel_details")
        put("description", "Obtient les détails complets d'un hôtel. Utilise cet outil quand l'utilisateur veut en savoir plus sur un hôtel précis.")
        put("input_schema", buildJsonObject {
            put("type", "object")
            put("properties", buildJsonObject {
                put("hotel_id", buildJsonObject {
                    put("type", "string")
                    put("description", "L'ID unique de l'hôtel (ex: abc123)")
                })
            })
            put("required", buildJsonArray { add("hotel_id") })
        })
    })
}

private suspend fun executeTool(
    name: String,
    input: JsonObject,
    hotelRepo: com.kamerstay.app.repository.HotelRepository
): String {
    return try {
        when (name) {
            "search_hotels" -> {
                val city = input["city"]?.jsonPrimitive?.content ?: ""
                val maxBudget = input["max_budget_fcfa"]?.jsonPrimitive?.intOrNull
                var hotels = hotelRepo.getHotelsByCity(city)
                if (maxBudget != null) hotels = hotels.filter { it.pricePerNight <= maxBudget }
                if (hotels.isEmpty()) {
                    "Aucun hôtel trouvé à $city${maxBudget?.let { " pour moins de $it FCFA/nuit" } ?: ""} sur KamerStay."
                } else {
                    buildString {
                        appendLine("${hotels.size} hôtel(s) trouvé(s) à $city :")
                        hotels.take(6).forEach { h ->
                            val fcfa = h.pricePerNight.toInt().toString().reversed().chunked(3).joinToString(" ").reversed()
                            val t = (h.rating * 10).toInt()
                            val rating = if (h.rating > 0) "★${t / 10}.${t % 10}" else "non noté"
                            val amenities = h.amenities.take(3).joinToString(", ").ifBlank { "standard" }
                            appendLine("• [ID:${h.id}] **${h.name}** | $fcfa FCFA/nuit | $rating | ${h.address} | $amenities")
                        }
                    }.trim()
                }
            }
            "get_hotel_details" -> {
                val hotelId = input["hotel_id"]?.jsonPrimitive?.content
                    ?: return "ID manquant"
                val hotel = hotelRepo.getHotelById(hotelId)
                    ?: return "Hôtel introuvable (ID: $hotelId)"
                val fcfa = hotel.pricePerNight.toInt().toString().reversed().chunked(3).joinToString(" ").reversed()
                val t = (hotel.rating * 10).toInt()
                val rating = if (hotel.rating > 0) "★${t / 10}.${t % 10} (${hotel.reviewCount} avis)" else "pas encore noté"
                buildString {
                    appendLine("**${hotel.name}**")
                    appendLine("Ville : ${hotel.city} | Adresse : ${hotel.address}")
                    appendLine("Prix : à partir de $fcfa FCFA/nuit")
                    appendLine("Note : $rating")
                    if (hotel.amenities.isNotEmpty()) appendLine("Équipements : ${hotel.amenities.joinToString(", ")}")
                    appendLine("Chambres : ${hotel.availableRooms} disponible(s) / ${hotel.totalRooms} au total")
                    if (hotel.phoneNumber.isNotBlank()) appendLine("Tél : ${hotel.phoneNumber}")
                    if (hotel.email.isNotBlank()) appendLine("Email : ${hotel.email}")
                    if (hotel.description.isNotBlank()) appendLine("Description : ${hotel.description.take(300)}")
                }.trim()
            }
            else -> "Outil '$name' inconnu."
        }
    } catch (e: Exception) { "Erreur outil $name : ${e.message}" }
}

// Stream depuis Anthropic, forward les text_delta au Writer, accumule les tool_use blocks.
// Retourne (toolCalls, stopReason).
private suspend fun streamFromAnthropic(
    bodyJson: JsonObject,
    writer: java.io.Writer,
    anthropicClient: HttpClient,
    apiKey: String
): Pair<List<ToolCallAcc>, String> {
    val toolCalls = mutableListOf<ToolCallAcc>()
    var currentTool: ToolCallAcc? = null
    var stopReason = "end_turn"

    anthropicClient.preparePost(ANTHROPIC_URL) {
        header("x-api-key", apiKey)
        header("anthropic-version", ANTHROPIC_VERSION)
        contentType(ContentType.Application.Json)
        setBody(bodyJson.toString())
    }.execute { httpResp ->
        val channel = httpResp.bodyAsChannel()
        while (!channel.isClosedForRead) {
            val line = channel.readUTF8Line() ?: break
            if (!line.startsWith("data:")) continue
            val data = line.removePrefix("data:").trim()
            if (data == "[DONE]") break
            try {
                val event = responseJson.parseToJsonElement(data).jsonObject
                when (event["type"]?.jsonPrimitive?.content) {
                    "content_block_start" -> {
                        val block = event["content_block"]?.jsonObject
                        when (block?.get("type")?.jsonPrimitive?.content) {
                            "tool_use" -> {
                                currentTool = ToolCallAcc(
                                    id   = block["id"]!!.jsonPrimitive.content,
                                    name = block["name"]!!.jsonPrimitive.content
                                )
                            }
                            else -> currentTool = null
                        }
                    }
                    "content_block_delta" -> {
                        val delta = event["delta"]?.jsonObject
                        when (delta?.get("type")?.jsonPrimitive?.content) {
                            "text_delta" -> {
                                val text = delta["text"]?.jsonPrimitive?.content
                                if (!text.isNullOrEmpty()) {
                                    writer.write("data: ${Json.encodeToString(text)}\n\n")
                                    writer.flush()
                                }
                            }
                            "input_json_delta" ->
                                currentTool?.inputJson?.append(delta["partial_json"]?.jsonPrimitive?.content ?: "")
                        }
                    }
                    "content_block_stop" -> {
                        currentTool?.let { toolCalls.add(it) }
                        currentTool = null
                    }
                    "message_delta" ->
                        stopReason = event["delta"]?.jsonObject?.get("stop_reason")?.jsonPrimitive?.content ?: "end_turn"
                }
            } catch (_: Exception) {}
        }
    }
    return Pair(toolCalls, stopReason)
}

private fun buildStreamingBody(
    messages: JsonArray,
    systemPromptText: String,
    withTools: Boolean = true
): JsonObject = buildJsonObject {
    put("model", CLAUDE_MODEL)
    put("max_tokens", 2048)
    put("temperature", 0.7)
    put("stream", true)
    put("system", buildJsonArray {
        add(buildJsonObject {
            put("type", "text")
            put("text", systemPromptText)
            put("cache_control", buildJsonObject { put("type", "ephemeral") })
        })
    })
    if (withTools) put("tools", CONCIERGE_TOOLS)
    put("messages", messages)
}

fun Route.aiRoutes(hotelRepo: com.kamerstay.app.repository.HotelRepository) {
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
                val rawText = callClaude(buildConciergePrompt(request), messages, apiKey)
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

        // ── Streaming SSE avec Tool Use ────────────────────────────────────────────
        post("/concierge/stream") {
            if (apiKey.isBlank()) {
                call.respond(HttpStatusCode.ServiceUnavailable,
                    ConciergeResponse("La clé API Anthropic n'est pas configurée."))
                return@post
            }

            val request = call.receive<ConciergeRequest>()

            // Prompt système enrichi avec contexte
            val systemPromptText = buildString {
                append(CONCIERGE_STREAMING_PROMPT)
                if (!request.hotelsContext.isNullOrBlank()) { append("\n\n"); append(request.hotelsContext) }
                if (!request.userContext.isNullOrBlank()) {
                    append("\n\nCONTEXTE UTILISATEUR :\n"); append(request.userContext)
                }
                append("\n\nOUTILS : Utilise search_hotels quand l'utilisateur demande des hôtels dans une ville ou avec un budget. Utilise get_hotel_details pour les détails d'un hôtel spécifique.")
            }

            // Messages initiaux au format JsonArray (supporte à la fois text et tool_result)
            var messagesJson: JsonArray = buildJsonArray {
                request.history.forEach { msg ->
                    add(buildJsonObject { put("role", msg.role); put("content", msg.content) })
                }
                add(buildJsonObject { put("role", "user"); put("content", request.message) })
            }

            call.response.header(HttpHeaders.CacheControl, "no-cache")
            call.response.header("X-Accel-Buffering", "no")
            call.respondTextWriter(contentType = ContentType.Text.EventStream) {
                try {
                    var iterations = 0
                    while (iterations++ < 3) {
                        val body = buildStreamingBody(messagesJson, systemPromptText)
                        val (toolCalls, stopReason) = streamFromAnthropic(
                            bodyJson = body,
                            writer = this,
                            anthropicClient = anthropicClient,
                            apiKey = apiKey
                        )

                        // Pas d'appel d'outil → conversation terminée
                        if (stopReason != "tool_use" || toolCalls.isEmpty()) break

                        // Construire le message assistant avec les tool_use blocks
                        val assistantMsg = buildJsonObject {
                            put("role", "assistant")
                            put("content", buildJsonArray {
                                toolCalls.forEach { tc ->
                                    add(buildJsonObject {
                                        put("type", "tool_use")
                                        put("id", tc.id)
                                        put("name", tc.name)
                                        put("input", try {
                                            responseJson.parseToJsonElement(tc.inputJson.toString()).jsonObject
                                        } catch (_: Exception) { buildJsonObject {} })
                                    })
                                }
                            })
                        }

                        // Exécuter les outils et construire les tool_result
                        val toolResultMsg = buildJsonObject {
                            put("role", "user")
                            put("content", buildJsonArray {
                                toolCalls.forEach { tc ->
                                    val input = try {
                                        responseJson.parseToJsonElement(tc.inputJson.toString()).jsonObject
                                    } catch (_: Exception) { buildJsonObject {} }
                                    val result = executeTool(tc.name, input, hotelRepo)
                                    add(buildJsonObject {
                                        put("type", "tool_result")
                                        put("tool_use_id", tc.id)
                                        put("content", result)
                                    })
                                }
                            })
                        }

                        // Ajouter les deux messages pour le prochain tour
                        messagesJson = buildJsonArray {
                            messagesJson.forEach { add(it) }
                            add(assistantMsg)
                            add(toolResultMsg)
                        }
                    }
                } catch (_: Exception) {
                    write("data: ${Json.encodeToString("Désolé, une erreur est survenue. Réessayez.")}\n\n")
                    flush()
                } finally {
                    write("data: [DONE]\n\n")
                    flush()
                }
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
                    apiKey,
                    temperature = 0.3  // JSON structuré → plus déterministe
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