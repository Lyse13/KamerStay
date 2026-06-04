package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NoResultState {
    var searchQuery by mutableStateOf("")
    var hasActiveFilters by mutableStateOf(true)
    var searchLocation by mutableStateOf("Douala")
    var isLoading by mutableStateOf(false)

    fun clearFilters() {
        hasActiveFilters = false
        searchQuery = ""
    }
}