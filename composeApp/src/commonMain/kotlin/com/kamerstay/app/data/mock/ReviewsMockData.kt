package com.kamerstay.app.data.mock

import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.model.CategoryRating
import com.kamerstay.app.data.model.GuestReview

object ReviewsMockData {

    val categoryRatings = listOf(
        CategoryRating("Cleanliness", 4.9),
        CategoryRating("Free Wi-Fi", 4.7),
        CategoryRating("Location", 5.0),
        CategoryRating("Service", 4.6)
    )

    val reviews = listOf(
        GuestReview(
            id = "1",
            name = "Jean-Pierre N.",
            initials = "JP",
            avatarColor = Color(0xFF8B6914),
            hasPhoto = true,
            stayType = "Verified Stay",
            date = "Oct 2023",
            rating = 5.0,
            comment = "\"An absolute gem in the heart of Douala. The concierge was exceptionally helpful with dinner reservations, and the room view of the harbor was breathtaking. Highly recommended for business travelers.\"",
            hasImages = true
        ),
        GuestReview(
            id = "2",
            name = "Marie-Claire E.",
            initials = "MC",
            avatarColor = Color(0xFF1A5276),
            hasPhoto = true,
            stayType = "Verified Stay",
            date = "Sep 2023",
            rating = 4.0,
            comment = "\"The breakfast buffet was the highlight of our stay! So many local and international options. Only minor issue was the elevator wait time during peak hours, but everything else was perfect.\"",
            tags = listOf("Excellent Breakfast", "Friendly Staff")
        ),
        GuestReview(
            id = "3",
            name = "Ahmed K.",
            initials = "AK",
            avatarColor = Color(0xFF00D5E1),
            stayType = "Verified Stay",
            date = "Aug 2023",
            rating = 5.0,
            comment = "\"The cleanest hotel I've stayed at in the region. The housekeeping team deserves a shoutout. Seamless check-in process through the app was a huge plus.\""
        )
    )
}