package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.ConciergeChatMessage
import com.kamerstay.app.data.model.SearchCriteria

data class UiChatMessage(
    val id: String,
    val role: String,
    val content: String,
    val isError: Boolean = false,
    val isWelcome: Boolean = false
)

class AiConciergeState {

    val messages = mutableStateListOf<UiChatMessage>()
    var isTyping by mutableStateOf(false)
    var extractedCriteria by mutableStateOf<SearchCriteria?>(null)
    var inputText by mutableStateOf("")

    private fun buildWelcome(): UiChatMessage {
        val name = UserSession.fullName.trim().ifBlank { null }
        val greeting = if (name != null) "Bonjour $name !" else "Bonjour !"
        return UiChatMessage(
            id = "welcome",
            role = "assistant",
            content = "$greeting Je suis Kamsa, votre concierge IA KamerStay 🇨🇲\n\nDites-moi ce que vous cherchez et je vous aide à trouver l'hôtel parfait au Cameroun !\n\nExemple : \"Je cherche un hôtel à Kribi pour le week-end, budget 50.000 FCFA\"",
            isWelcome = true
        )
    }

    init {
        messages.add(buildWelcome())
    }

    fun addUserMessage(content: String) {
        messages.add(UiChatMessage(id = "u_${messages.size}", role = "user", content = content))
    }

    fun addAssistantMessage(content: String, isError: Boolean = false) {
        messages.add(UiChatMessage(id = "a_${messages.size}", role = "assistant", content = content, isError = isError))
    }

    fun historyForApi(): List<ConciergeChatMessage> {
        // Tous les messages réels (pas bienvenue, pas erreur), sans le dernier (= message user en cours)
        val real = messages.filter { !it.isWelcome && !it.isError }
        val withoutCurrent = if (real.size > 1) real.dropLast(1) else return emptyList()
        // Garantit l'alternance user/assistant exigée par l'API Anthropic
        return withoutCurrent.fold(emptyList()) { acc, msg ->
            when {
                acc.isEmpty() && msg.role != "user" -> acc
                acc.isEmpty() -> listOf(ConciergeChatMessage(msg.role, msg.content))
                acc.last().role == msg.role -> acc
                else -> acc + ConciergeChatMessage(msg.role, msg.content)
            }
        }
    }

    fun reset() {
        messages.clear()
        extractedCriteria = null
        inputText = ""
        messages.add(buildWelcome())
    }
}