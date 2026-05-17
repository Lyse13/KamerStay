package com.kamerstay.app.model

import com.kamerstay.app.model.enums.LandmarkType
import kotlinx.serialization.Serializable

@Serializable
data class Landmark(
    val id: String = "",
    val name: String = "",
    val type: LandmarkType = LandmarkType.OTHER,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = ""
)
