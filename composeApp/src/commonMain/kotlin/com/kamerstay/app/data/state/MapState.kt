package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MapState {
    var searchQuery by mutableStateOf("Douala, Cameroon")
    var selectedHotelId by mutableStateOf("1")
    var zoomLevel by mutableStateOf(14f)
    var isLoading by mutableStateOf(false)
}