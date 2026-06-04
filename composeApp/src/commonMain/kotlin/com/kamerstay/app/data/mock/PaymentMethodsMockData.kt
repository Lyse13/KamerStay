package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.AlternativeHotel
import com.kamerstay.app.data.model.PaymentMethod
import com.kamerstay.app.data.model.PaymentMethodType

object PaymentMethodsMockData {

    val primaryAccount = PaymentMethod(
        id = "1",
        name = "Akwa Business Account",
        detail = "•••• 5928",
        type = PaymentMethodType.BANK,
        isActive = true
    )

    val secondaryMethods = listOf(
        PaymentMethod(
            id = "2",
            name = "Orange Money",
            detail = "+237 6xx xxx 44",
            type = PaymentMethodType.MOBILE_MONEY
        ),
        PaymentMethod(
            id = "3",
            name = "Corporate Visa",
            detail = "Expires 09/27",
            type = PaymentMethodType.CARD
        )
    )

    val alternativeHotels = listOf(
        AlternativeHotel(
            id = "1",
            name = "Azure Bay Retreat",
            location = "Malibu, California",
            pricePerNight = 240,
            tags = listOf("WIFI", "POOL"),
            gradientColors = listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A))
        ),
        AlternativeHotel(
            id = "2",
            name = "Prussian Blue Estates",
            location = "Santorini, Greece",
            pricePerNight = 310,
            tags = listOf("BREAKFAST", "SPA"),
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
        AlternativeHotel(
            id = "3",
            name = "Pine Peak Modern",
            location = "Aspen, Colorado",
            pricePerNight = 195,
            tags = listOf("FIREPLACE", "GYM"),
            gradientColors = listOf(Color(0xFF1A3A2E), Color(0xFF0D2218))
        )
    )
}