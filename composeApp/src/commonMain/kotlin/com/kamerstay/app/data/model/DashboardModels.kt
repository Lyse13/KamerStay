package com.kamerstay.app.data.model

import androidx.compose.ui.graphics.Color

data class RecentActivity(
    val title: String,
    val room: String,
    val time: String,
    val badge: String,
    val badgeColor: Color,
    val badgeTextColor: Color
)