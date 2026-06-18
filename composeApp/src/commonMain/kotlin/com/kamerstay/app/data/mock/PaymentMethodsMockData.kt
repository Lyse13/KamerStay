package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.AlternativeHotel
import com.kamerstay.app.data.model.PaymentMethod
import com.kamerstay.app.data.model.PaymentMethodType

object PaymentMethodsMockData {

    val primaryAccount = PaymentMethod(
        id = "1",
        name = "Compte Professionnel Akwa",
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
            name = "Visa Entreprise",
            detail = "Expire 09/27",
            type = PaymentMethodType.CARD
        )
    )

    val alternativeHotels = listOf(
        AlternativeHotel(
            id = "1",
            name = "Atlantic Beach Resort",
            location = "Limbé, Cameroun",
            pricePerNight = 83_000,
            tags = listOf("WI-FI", "PISCINE"),
            gradientColors = listOf(Color(0xFF1A3A5C), Color(0xFF0D2A4A))
        ),
        AlternativeHotel(
            id = "2",
            name = "La Falaise des Bamboutos",
            location = "Bafoussam, Cameroun",
            pricePerNight = 47_000,
            tags = listOf("PETIT-DÉJ", "SPA"),
            gradientColors = listOf(Color(0xFF1A2A3A), Color(0xFF0D1A28))
        ),
        AlternativeHotel(
            id = "3",
            name = "Résidence Chutes de la Lobé",
            location = "Kribi, Cameroun",
            pricePerNight = 45_000,
            tags = listOf("PLAGE", "RESTO"),
            gradientColors = listOf(Color(0xFF1A3A2E), Color(0xFF0D2218))
        )
    )
}