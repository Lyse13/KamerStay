package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ReviewState {
    var selectedFilter by mutableStateOf("All Reviews")
    var isLoading by mutableStateOf(false)
    var newReviewText by mutableStateOf("")
    var newReviewRating by mutableStateOf(0)
}