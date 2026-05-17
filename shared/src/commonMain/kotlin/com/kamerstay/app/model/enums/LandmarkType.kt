package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class LandmarkType {
    HOSPITAL,
    UNIVERSITY,
    STADIUM,
    MARKET,
    TRANSPORT_AGENCY,
    ADMINISTRATIVE,
    AIRPORT,
    BUS_STATION,
    OTHER
}