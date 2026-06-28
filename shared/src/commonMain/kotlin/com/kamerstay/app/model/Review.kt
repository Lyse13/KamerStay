package com.kamerstay.app.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String = "",
    val hotelId: String = "",
    val travelerId: String = "",
    val travelerName: String = "",
    val rating: Double = 0.0,
    val comment: String = "",
    val createdAt: String = ""
)