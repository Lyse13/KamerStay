package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.ConciergeChatMessage
import com.kamerstay.app.data.model.SearchCriteria
import kotlinx.serialization.Serializable

@Serializable
data class UiChatMessage(
    val id: String,
    val role: String,
    val content: String,
    val isError: Boolean = false,
    val isWelcome: Boolean = false,
    val isProactive: Boolean = false,
    val isStreaming: Boolean = false
)

fun getQuickReplies(): List<String> = if (UserSession.language == "en") listOf(
    "Hotels in Douala",
    "Hotels in Kribi",
    "Max budget 50,000 FCFA",
    "My current booking",
    "Luxury hotels"
) else listOf(
    "Hôtels à Douala",
    "Hôtels à Kribi",
    "Budget max 50.000 FCFA",
    "Ma réservation en cours",
    "Hôtels de luxe"
)

class AiConciergeState {

    // ── Limites de l'historique ───────────────────────────────
    // MAX_HISTORY_FOR_API : messages envoyés à Claude à chaque requête (10 échanges).
    // Évite d'envoyer toute la conversation, réduit coût et latence.
    // MAX_STORED_MESSAGES : messages conservés sur disque entre sessions (20 échanges).
    // L'affichage dans l'app n'est PAS limité — l'utilisateur voit toute la conversation.
    private val MAX_HISTORY_FOR_API = 20
    private val MAX_STORED_MESSAGES = 40

    val messages = mutableStateListOf<UiChatMessage>()
    var isTyping by mutableStateOf(false)
    var extractedCriteria by mutableStateOf<SearchCriteria?>(null)
    var inputText by mutableStateOf("")
    var hasShownProactive by mutableStateOf(false)
    var streamingMessageId by mutableStateOf<String?>(null)
        private set

    private var idCounter = 0

    private fun buildWelcome(): UiChatMessage {
        val name = UserSession.fullName.trim().ifBlank { null }
        val content = if (UserSession.language == "en") {
            val greeting = if (name != null) "Hello $name!" else "Hello!"
            "$greeting I'm **Kamsa**, your KamerStay AI concierge 🇨🇲\n\nTell me what you're looking for and I'll help you find the perfect hotel in Cameroon!\n\nExample: *\"I'm looking for a hotel in Kribi for the weekend, budget 50,000 FCFA\"*"
        } else {
            val greeting = if (name != null) "Bonjour $name !" else "Bonjour !"
            "$greeting Je suis **Kamsa**, votre concierge IA KamerStay 🇨🇲\n\nDites-moi ce que vous cherchez et je vous aide à trouver l'hôtel parfait au Cameroun !\n\nExemple : *\"Je cherche un hôtel à Kribi pour le week-end, budget 50.000 FCFA\"*"
        }
        return UiChatMessage(
            id        = "welcome",
            role      = "assistant",
            content   = content,
            isWelcome = true
        )
    }

    init {
        messages.add(buildWelcome())
    }

    fun addUserMessage(content: String) {
        messages.add(UiChatMessage(id = "msg_${idCounter++}", role = "user", content = content))
    }

    fun addAssistantMessage(content: String, isError: Boolean = false) {
        messages.add(UiChatMessage(id = "msg_${idCounter++}", role = "assistant", content = content, isError = isError))
    }

    fun beginStreamingMessage() {
        val id = "stream_${idCounter++}"
        streamingMessageId = id
        messages.add(UiChatMessage(id = id, role = "assistant", content = "", isStreaming = true))
    }

    fun appendStreamingChunk(chunk: String) {
        val idx = messages.indexOfFirst { it.id == streamingMessageId }
        if (idx >= 0) messages[idx] = messages[idx].copy(content = messages[idx].content + chunk)
    }

    fun finalizeStreamingMessage() {
        val idx = messages.indexOfFirst { it.id == streamingMessageId }
        if (idx >= 0) messages[idx] = messages[idx].copy(isStreaming = false)
        streamingMessageId = null
    }

    fun addProactiveMessage(content: String) {
        if (hasShownProactive) return
        hasShownProactive = true
        messages.add(1, UiChatMessage(id = "msg_${idCounter++}", role = "assistant", content = content, isProactive = true))
    }

    val hasOnlyWelcome: Boolean get() = messages.none { !it.isWelcome && !it.isProactive }

    fun historyForApi(): List<ConciergeChatMessage> {
        // Exclut : bienvenue, erreurs, placeholder streaming vide
        val real = messages.filter { !it.isWelcome && !it.isError && !(it.isStreaming && it.content.isEmpty()) }
        val withoutCurrent = if (real.size > 1) real.dropLast(1) else return emptyList()

        // Tronquer aux MAX_HISTORY_FOR_API derniers messages.
        // Les messages plus anciens ne sont plus envoyés à Claude, mais restent visibles dans l'app.
        val windowed = withoutCurrent.takeLast(MAX_HISTORY_FOR_API)

        // Garantit l'alternance user/assistant exigée par l'API Anthropic
        return windowed.fold(emptyList()) { acc, msg ->
            when {
                acc.isEmpty() && msg.role != "user" -> acc
                acc.isEmpty() -> listOf(ConciergeChatMessage(msg.role, msg.content))
                acc.last().role == msg.role -> acc
                else -> acc + ConciergeChatMessage(msg.role, msg.content)
            }
        }
    }

    fun serializeMessages(): String {
        // Limite les messages sauvegardés sur disque aux MAX_STORED_MESSAGES derniers.
        // Les messages plus anciens sont abandonnés entre les sessions.
        val toSave = messages
            .filter { !it.isWelcome && !it.isError && !it.isStreaming }
            .takeLast(MAX_STORED_MESSAGES)
        return kotlinx.serialization.json.Json.encodeToString(
            kotlinx.serialization.builtins.ListSerializer(UiChatMessage.serializer()),
            toSave
        )
    }

    fun restoreMessages(json: String) {
        try {
            val saved = kotlinx.serialization.json.Json.decodeFromString(
                kotlinx.serialization.builtins.ListSerializer(UiChatMessage.serializer()),
                json
            )
            if (saved.isNotEmpty()) {
                messages.clear()
                messages.add(buildWelcome())
                // Réattribue un id unique à chaque message restauré
                // pour éviter toute collision avec les futurs messages (msg_0, msg_1...)
                saved.forEach { msg ->
                    messages.add(msg.copy(id = "restored_${idCounter++}"))
                }
            }
        } catch (_: Exception) { /* Si JSON corrompu, on repart à zéro */ }
    }

    fun reset() {
        messages.clear()
        extractedCriteria = null
        inputText = ""
        hasShownProactive = false
        streamingMessageId = null
        idCounter = 0
        messages.add(buildWelcome())
    }
}