package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class KpiMetric(
    val id: String,
    val icon: String,
    val label: String,
    val value: String,
    val change: String,
    val isPositive: Boolean
)

data class OccupancyItem(
    val label: String,
    val percentage: Int
)

data class StaffPerformance(
    val id: String,
    val name: String,
    val role: String,
    val score: Int,
    val badge: String,
    val badgeColor: Color,
    val avatarColor: Color,
    val initials: String
)

data class RevenueBar(
    val month: String,
    val targetHeight: Float,
    val actualHeight: Float
)

data class RevenueForecast(
    val description: String,
    val estimatedAmount: String,
    val growthPercent: Int
)

