package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.SupportCategory
import com.kamerstay.app.data.model.TrendingTopic

object TravelerSupportMockData {

    val categories = listOf(
        SupportCategory(
            id = "1", icon = "booking",
            title = "Bookings",
            subtitle = "Manage, modify or cancel your reservations with ease."
        ),
        SupportCategory(
            id = "2", icon = "payments",
            title = "Payments",
            subtitle = "Invoices, refunds, and secure payment methods."
        ),
        SupportCategory(
            id = "3", icon = "account",
            title = "Account",
            subtitle = "Security, preferences, and loyalty program details."
        ),
        SupportCategory(
            id = "4", icon = "travel",
            title = "Travel Guide",
            subtitle = "Visa requirements, check-in tips, and city guides."
        )
    )

    val popularQuestions = listOf(
        TrendingTopic("1", "How do I cancel my booking?"),
        TrendingTopic("2", "What is the refund policy?"),
        TrendingTopic("3", "Can I pay upon arrival?"),
        TrendingTopic("4", "Where can I find my invoice?")
    )

    val popularAnswers = mapOf(
        "1" to "Go to \"My Bookings\", select the reservation you wish to cancel, and click the \"Cancel Booking\" button.",
        "2" to "Refund policies vary by property. Check your confirmation email for specific terms of your stay.",
        "3" to "Some properties allow \"Pay at Hotel.\" This option will be visible during the checkout process if available.",
        "4" to "Once your stay is completed, your invoice will be available in the \"Payments\" section of your profile."
    )
}