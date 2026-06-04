package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.mock.TravelerPaymentMockData
import com.kamerstay.app.data.model.TravelerCard

class TravelerPaymentMethodsState {
    var selectedCardId by mutableStateOf("1")
    var showAddDialog by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var cards by mutableStateOf<List<TravelerCard>>(TravelerPaymentMockData.savedCards)
    val primaryCard get() = TravelerPaymentMockData.primaryCard
}