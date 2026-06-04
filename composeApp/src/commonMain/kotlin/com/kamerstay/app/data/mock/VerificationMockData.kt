package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.*

object VerificationMockData {

    val documents = listOf(
        VerificationDocument(
            id = "1",
            type = DocumentType.NATIONAL_ID_FRONT
        ),
        VerificationDocument(
            id = "2",
            type = DocumentType.NATIONAL_ID_BACK
        ),
        VerificationDocument(
            id = "3",
            type = DocumentType.BUSINESS_LICENSE
        )
    )

    val requirements = listOf(
        "Document must be valid and not expired.",
        "All 4 corners of the document must be visible.",
        "Text must be clear and legible.",
        "No glare or shadows obscuring the info."
    )
}

object AmenitiesMockData {

    val amenities = listOf(
        AmenityItem(
            id = "1",
            icon = "wifi",
            name = "High-Speed WiFi",
            description = "Fiber optic connectivity throughout the property.",
            category = AmenityCategory.ESSENTIAL,
            isEnabled = true
        ),
        AmenityItem(
            id = "2",
            icon = "ac",
            name = "Climate Control",
            description = "Central HVAC with individual room thermostats.",
            category = AmenityCategory.ESSENTIAL,
            isEnabled = true
        ),
        AmenityItem(
            id = "3",
            icon = "pool",
            name = "Swimming Pool",
            description = "Heated infinity pool access",
            category = AmenityCategory.LUXURY_WELLNESS,
            isEnabled = true
        ),
        AmenityItem(
            id = "4",
            icon = "spa",
            name = "Spa & Wellness",
            description = "Full-service spa with massage and sauna",
            category = AmenityCategory.LUXURY_WELLNESS,
            isEnabled = false
        ),
        AmenityItem(
            id = "5",
            icon = "restaurant",
            name = "Restaurant",
            description = "Fine dining with local and international cuisine",
            category = AmenityCategory.DINING,
            isEnabled = true
        ),
        AmenityItem(
            id = "6",
            icon = "gym",
            name = "Fitness Center",
            description = "24/7 fully equipped gym",
            category = AmenityCategory.LUXURY_WELLNESS,
            isEnabled = false
        )
    )
}