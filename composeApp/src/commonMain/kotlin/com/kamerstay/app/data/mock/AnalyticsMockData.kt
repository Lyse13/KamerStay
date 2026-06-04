package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.*

object AnalyticsMockData {

    val kpiMetrics = listOf(
        KpiMetric(
            id = "1",
            icon = "payments",
            label = "TOTAL REVENUE",
            value = "CFA 42,850,000",
            change = "+12.5%",
            isPositive = true
        ),
        KpiMetric(
            id = "2",
            icon = "trending_up",
            label = "AVG. DAILY RATE",
            value = "CFA 85,000",
            change = "+4.2%",
            isPositive = true
        ),
        KpiMetric(
            id = "3",
            icon = "book_online",
            label = "ACTIVE BOOKINGS",
            value = "1,248",
            change = "-2.1%",
            isPositive = false
        )
    )

    val revenueBars = listOf(
        RevenueBar("Jan", 0.6f, 0.5f),
        RevenueBar("Feb", 0.75f, 0.65f),
        RevenueBar("Mar", 0.55f, 0.7f),
        RevenueBar("Apr", 0.8f, 0.6f),
        RevenueBar("May", 0.7f, 0.85f),
        RevenueBar("Jun", 0.65f, 0.75f)
    )

    val currentOccupancy = 84.2f
    val occupancyChange = "+2.4% from last night"

    val occupancyItems = listOf(
        OccupancyItem("Suites", 92),
        OccupancyItem("Deluxe Rooms", 78),
        OccupancyItem("Standard", 86)
    )

    val staffPerformance = listOf(
        StaffPerformance(
            id = "1",
            name = "Amina B.",
            role = "Douala Region Manager",
            score = 98,
            badge = "Top Tier",
            badgeColor = Color(0xFF00D5E1),
            avatarColor = Color(0xFF8B6914),
            initials = "AB"
        ),
        StaffPerformance(
            id = "2",
            name = "Samuel K.",
            role = "Operations Supervisor",
            score = 92,
            badge = "Excellent",
            badgeColor = Color(0xFF2E7D32),
            avatarColor = Color(0xFF1A3A5C),
            initials = "SK"
        )
    )

    val forecast = RevenueForecast(
        description = "Based on historical data and current booking trends, next month's revenue is projected to increase by 15% due to seasonal demand.",
        estimatedAmount = "CFA 49M",
        growthPercent = 15
    )
}