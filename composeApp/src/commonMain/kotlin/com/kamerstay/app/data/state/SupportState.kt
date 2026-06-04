package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SupportState {
    var searchQuery by mutableStateOf("")
    var isLoading by mutableStateOf(false)
}