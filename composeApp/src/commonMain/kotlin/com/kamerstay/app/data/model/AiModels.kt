package com.kamerstay.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ConciergeChatMessage(
    val role: String,
    val content: String
)

@Serializable
data class ConciergeRequest(
    val message: String,
    val history: List<ConciergeChatMessage> = emptyList(),
    val userName: String? = null
)

@Serializable
data class SearchCriteria(
    val city: String? = null,
    val budgetFcfa: Int? = null,
    val checkIn: String? = null,
    val checkOut: String? = null,
    val travelType: String? = null,
    val amenities: List<String> = emptyList()
) {
    fun hasContent() = city != null || budgetFcfa != null
}

@Serializable
data class ConciergeResponse(
    val message: String,
    val criteria: SearchCriteria? = null
)