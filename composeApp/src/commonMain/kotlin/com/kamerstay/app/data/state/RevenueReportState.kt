package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RevenueReportState {
    var selectedTab by mutableStateOf("Revenue")
    var isLoading by mutableStateOf(false)
    var period by mutableStateOf("Q3 2023")
    var propertyName by mutableStateOf("Grand Hyatt Downtown")
}