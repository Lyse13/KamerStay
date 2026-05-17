package com.kamerstay.app.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RoomType {
    SINGLE,
    DOUBLE,
    TWIN,
    SUITE,
    FAMILY,
    DELUXE
}