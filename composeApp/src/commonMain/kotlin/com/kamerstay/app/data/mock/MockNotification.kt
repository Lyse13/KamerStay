package com.kamerstay.app.data.mock

data class MockNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: String, // BOOKING, PAYMENT, PROMO, SYSTEM
    val isRead: Boolean = false,
    val hasImage: Boolean = false,
    val highlightText: String = ""
)

val mockNotifications = listOf(
    MockNotification(
        id = "1",
        title = "Booking Confirmed",
        message = "Your stay at Palais de la Renaissance in Yaoundé is confirmed. Prepare for a royal experience.",
        time = "2h ago",
        type = "BOOKING",
        isRead = false,
        highlightText = "Palais de la Renaissance"
    ),
    MockNotification(
        id = "2",
        title = "Payment Received",
        message = "Payment for your reservation at Kribi Beach Resort (Ref: #ST882) has been processed successfully via Mobile Money.",
        time = "5h ago",
        type = "PAYMENT",
        isRead = false
    ),
    MockNotification(
        id = "3",
        title = "Weekend Getaway",
        message = "Enjoy 20% off on all Douala hotels this weekend. Use code SAHEL20 at checkout.",
        time = "Yesterday",
        type = "PROMO",
        isRead = true,
        hasImage = true,
        highlightText = "SAHEL20"
    ),
    MockNotification(
        id = "4",
        title = "Profile Updated",
        message = "Your identity verification has been completed. You are now a 'Verified Traveler' on KamerStay.",
        time = "2 days ago",
        type = "SYSTEM",
        isRead = true
    ),
)
