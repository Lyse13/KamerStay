package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.*

object SupportMockData {

    val categories = listOf(
        SupportCategory(
            id = "1", icon = "payments",
            title = "Payouts & Finance",
            subtitle = "Tracking statements and bank details",
            isFeatured = true
        ),
        SupportCategory(id = "2", icon = "booking", title = "Booking Issues"),
        SupportCategory(id = "3", icon = "tech", title = "Technical Support"),
        SupportCategory(id = "4", icon = "shield", title = "Safety & Security"),
        SupportCategory(id = "5", icon = "settings", title = "Property Settings")
    )

    val contacts = listOf(
        SupportContact(
            id = "1", icon = "chat",
            title = "Live Chat",
            subtitle = "Wait time: ~2 mins"
        ),
        SupportContact(
            id = "2", icon = "email",
            title = "Email Support",
            subtitle = "Response within 24 hours"
        )
    )

    val trendingTopics = listOf(
        TrendingTopic("1", "How to update room availability?"),
        TrendingTopic("2", "Setting up seasonal pricing"),
        TrendingTopic("3", "Resolving a double booking"),
        TrendingTopic("4", "Canceling a guest reservation")
    )
}