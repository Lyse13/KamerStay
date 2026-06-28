package com.kamerstay.app.repository

import com.kamerstay.app.config.DatabaseConfig
import com.kamerstay.app.model.Review
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.toList

class ReviewRepository {
    private val reviews = DatabaseConfig.reviewsCollection

    suspend fun getReviewsByHotel(hotelId: String): List<Review> {
        return reviews
            .find(Filters.eq("hotelId", hotelId))
            .sort(Sorts.descending("createdAt"))
            .toList()
    }

    suspend fun createReview(review: Review) {
        reviews.insertOne(review)
    }

    suspend fun averageRatingForHotel(hotelId: String): Double {
        val all = getReviewsByHotel(hotelId)
        return if (all.isEmpty()) 0.0 else all.sumOf { it.rating } / all.size
    }
}