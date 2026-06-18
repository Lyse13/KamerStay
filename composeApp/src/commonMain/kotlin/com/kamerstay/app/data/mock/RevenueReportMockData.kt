package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.*

object RevenueReportMockData {

    val metrics = listOf(
        RevenueMetric(
            id = "1", icon = "payments",
            label = "Revenu Total", value = "170 754 000 FCFA",
            change = "+12.5%", isPositive = true
        ),
        RevenueMetric(
            id = "2", icon = "bed",
            label = "Taux d'occupation", value = "88.4%",
            change = "+3.2%", isPositive = true
        ),
        RevenueMetric(
            id = "3", icon = "chart",
            label = "Tarif Journalier Moy.", value = "187 470 FCFA",
            change = "+8.1%", isPositive = true
        ),
        RevenueMetric(
            id = "4", icon = "trending",
            label = "RevPAR", value = "165 720 FCFA",
            change = "+5.7%", isPositive = true
        )
    )

    val monthlyEarnings = listOf(
        MonthlyEarning("JAN", 0.35f, 0.30f),
        MonthlyEarning("FÉV", 0.45f, 0.40f),
        MonthlyEarning("MAR", 0.50f, 0.45f),
        MonthlyEarning("AVR", 0.55f, 0.50f),
        MonthlyEarning("MAI", 0.60f, 0.55f),
        MonthlyEarning("JUN", 0.65f, 0.60f),
        MonthlyEarning(
            "JUL", 0.90f, 0.80f,
            isHighlighted = true, highlightLabel = "23M"
        ),
        MonthlyEarning("AOÛ", 0.75f, 0.70f),
        MonthlyEarning("SEP", 0.55f, 0.50f)
    )

    val paymentMethods = listOf(
        PaymentMethodShare("card", "Carte Bancaire", 28),
        PaymentMethodShare("bank", "Virement Direct", 18),
        PaymentMethodShare("wallet", "MTN Mobile Money", 32),
        PaymentMethodShare("other", "Orange Money", 22)
    )

    val recentTransactions = listOf(
        RecentTransaction(
            id = "1",
            guestName = "Nadège Belinga",
            roomType = "King Deluxe",
            nights = 3,
            date = "28 Jul 2024",
            status = TransactionStatus.COMPLETED,
            amount = "747 000 FCFA",
            initials = "NB",
            avatarColor = Color(0xFF1A5276)
        ),
        RecentTransaction(
            id = "2",
            guestName = "Emmanuel Mbia",
            roomType = "Suite Executive",
            nights = 2,
            date = "27 Jul 2024",
            status = TransactionStatus.COMPLETED,
            amount = "1 260 000 FCFA",
            initials = "EM",
            avatarColor = Color(0xFF8B4513)
        ),
        RecentTransaction(
            id = "3",
            guestName = "Estelle Nkengfack",
            roomType = "Queen Standard",
            nights = 1,
            date = "27 Jul 2024",
            status = TransactionStatus.PENDING,
            amount = "348 000 FCFA",
            initials = "EN",
            avatarColor = Color(0xFF1A3A2E)
        )
    )
}