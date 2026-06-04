package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AnalyticsState {
    var selectedPeriod by mutableStateOf("Week")
    var isLoading by mutableStateOf(false)
    var currentOccupancy by mutableStateOf(84.2f)
    var totalRevenue by mutableStateOf("CFA 42,850,000")
    var avgDailyRate by mutableStateOf("CFA 85,000")
    var activeBookings by mutableStateOf("1,248")
}