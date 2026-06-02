package com.kamerstay.app.data.mock

import com.kamerstay.app.core.theme.*
import com.kamerstay.app.data.model.RecentActivity

object DashboardMockData {

    val recentActivities = listOf(
        RecentActivity(
            title = "Check-in: Felix Anderson",
            room = "Room 402 • Deluxe Ocean View",
            time = "10:45 AM",
            badge = "Active",
            badgeColor = Primary.copy(0.15f),
            badgeTextColor = Secondary
        ),
        RecentActivity(
            title = "New Booking: Sarah Miller",
            room = "Room 105 • Junior Suite • 3 Nights",
            time = "09:12 AM",
            badge = "Confirmed",
            badgeColor = OnSurfaceSecondary.copy(0.15f),
            badgeTextColor = TextDark
        ),
        RecentActivity(
            title = "Check-out: Marco Rossi",
            room = "Room 218 • Standard Double",
            time = "08:30 AM",
            badge = "Departed",
            badgeColor = OnSurfaceSecondary.copy(0.1f),
            badgeTextColor = OnSurfaceSecondary
        ),
    )

    val revenueBarHeights = listOf(0.4f, 0.55f, 0.5f, 0.7f, 0.65f, 0.8f, 1f)
}