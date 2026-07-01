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
) {
    fun hasContent() = city != null || budgetFcfa != null
    fun hasReadyToBook() = city != null && checkIn != null && checkOut != null
}

@Serializable
data class ConciergeResponse(
    val message: String,
    val criteria: SearchCriteria? = null
)

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