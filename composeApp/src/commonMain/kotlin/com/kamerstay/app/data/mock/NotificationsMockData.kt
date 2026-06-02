package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.AppNotification
import com.kamerstay.app.data.model.NotificationType

object NotificationsMockData {

    val todayNotifications = listOf(
        AppNotification(
            id = "1",
            title = "Booking Confirmed",
            message = "Your stay at The Azure Resort is confirmed for Aug 15–20. We look forward to seeing you!",
            time = "10:24 AM",
            type = NotificationType.BOOKING,
            isRead = false
        ),
        AppNotification(
            id = "2",
            title = "Ready for Check-in?",
            message = "Check-in for Room 402 is now open. Use the app for a contactless arrival experience.",
            time = "08:15 AM",
            type = NotificationType.CHECK_IN,
            isRead = false,
            hasAction = true,
            actionLabel = "Start Check-in"
        ),
        AppNotification(
            id = "3",
            title = "Maintenance Alert",
            message = "HVAC system update required in East Wing, Room 204. Scheduled for 11:00 AM.",
            time = "07:00 AM",
            type = NotificationType.ALERT,
            isRead = false,
            isAlert = true
        ),
    )

    val earlierNotifications = listOf(
        AppNotification(
            id = "4",
            title = "Weekend Escape Deal",
            message = "Get 20% off your next booking at any urban location. Offer ends this Sunday!",
            time = "Yesterday",
            type = NotificationType.PROMO,
            isRead = true
        ),
        AppNotification(
            id = "5",
            title = "Payment Received",
            message = "Transaction ID #88123 for \$540.00 was successful for your July stay.",
            time = "Jul 10",
            type = NotificationType.PAYMENT,
            isRead = true
        ),
    )
}