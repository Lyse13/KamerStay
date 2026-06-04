package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.*

object RevenueReportMockData {

    val metrics = listOf(
        RevenueMetric(
            id = "1", icon = "payments",
            label = "Total Revenue", value = "\$284,590.00",
            change = "+12.5%", isPositive = true
        ),
        RevenueMetric(
            id = "2", icon = "bed",
            label = "Occupancy Rate", value = "88.4%",
            change = "+3.2%", isPositive = true
        ),
        RevenueMetric(
            id = "3", icon = "chart",
            label = "Avg Daily Rate", value = "\$312.45",
            change = "+8.1%", isPositive = true
        ),
        RevenueMetric(
            id = "4", icon = "trending",
            label = "RevPAR", value = "\$276.20",
            change = "+5.7%", isPositive = true
        )
    )

    val monthlyEarnings = listOf(
        MonthlyEarning("JAN", 0.35f, 0.30f),
        MonthlyEarning("FEB", 0.45f, 0.40f),
        MonthlyEarning("MAR", 0.50f, 0.45f),
        MonthlyEarning("APR", 0.55f, 0.50f),
        MonthlyEarning("MAY", 0.60f, 0.55f),
        MonthlyEarning("JUN", 0.65f, 0.60f),
        MonthlyEarning(
            "JUL", 0.90f, 0.80f,
            isHighlighted = true, highlightLabel = "\$38k"
        ),
        MonthlyEarning("AUG", 0.75f, 0.70f),
        MonthlyEarning("SEP", 0.55f, 0.50f)
    )

    val paymentMethods = listOf(
        PaymentMethodShare("card", "Credit Card", 64),
        PaymentMethodShare("bank", "Direct Transfer", 22),
        PaymentMethodShare("wallet", "Digital Wallets", 11),
        PaymentMethodShare("other", "Others", 3)
    )

    val recentTransactions = listOf(
        RecentTransaction(
            id = "1",
            guestName = "Julianne Smith",
            roomType = "Deluxe King",
            nights = 3,
            date = "Jul 28, 2023",
            status = TransactionStatus.COMPLETED,
            amount = "\$1,245",
            initials = "JS",
            avatarColor = Color(0xFF1A5276)
        ),
        RecentTransaction(
            id = "2",
            guestName = "Marcus Reed",
            roomType = "Executive Suite",
            nights = 2,
            date = "Jul 27, 2023",
            status = TransactionStatus.COMPLETED,
            amount = "\$2,100",
            initials = "MR",
            avatarColor = Color(0xFF8B4513)
        ),
        RecentTransaction(
            id = "3",
            guestName = "Lydia Bennett",
            roomType = "Standard Queen",
            nights = 1,
            date = "Jul 27, 2023",
            status = TransactionStatus.PENDING,
            amount = "\$580",
            initials = "LB",
            avatarColor = Color(0xFF1A3A2E)
        )
    )
}