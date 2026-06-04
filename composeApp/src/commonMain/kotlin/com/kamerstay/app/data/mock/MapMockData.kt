package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.MapHotel

object MapMockData {

    // Coordonnées réelles de Douala, Cameroun
    val DOUALA_LAT = 4.0511
    val DOUALA_LNG = 9.7679

    val hotels = listOf(
        MapHotel(
            id = "1",
            name = "Akwa Palace Luxury",
            price = 185,
            rating = 4.8,
            distance = "2.4 km",
            location = "Akwa District",
            amenities = listOf("WI-FI", "POOL"),
            lat = 4.0511,
            lng = 9.7679,
            isSelected = true
        ),
        MapHotel(
            id = "2",
            name = "Bonapriso Suites",
            price = 120,
            rating = 4.5,
            distance = "1.8 km",
            location = "Bonapriso",
            amenities = listOf("WI-FI", "GYM"),
            lat = 4.0450,
            lng = 9.7580
        ),
        MapHotel(
            id = "3",
            name = "Bonanjo Business Hotel",
            price = 95,
            rating = 4.2,
            distance = "3.1 km",
            location = "Bonanjo",
            amenities = listOf("WI-FI", "PARKING"),
            lat = 4.0600,
            lng = 9.7750
        )
    )
}