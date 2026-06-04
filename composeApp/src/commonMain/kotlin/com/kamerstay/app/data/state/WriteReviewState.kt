package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class WriteReviewState {
    var selectedRating by mutableIntStateOf(0)
    var reviewText by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isSubmitted by mutableStateOf(false)
    var photoCount by mutableIntStateOf(2)
}