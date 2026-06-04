package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.PaymentMethod
import com.kamerstay.app.data.mock.PaymentMethodsMockData

class PaymentMethodsState {
    var selectedMethodId by mutableStateOf("1")
    var isLoading by mutableStateOf(false)
    var showAddDialog by mutableStateOf(false)
    var methods by mutableStateOf(PaymentMethodsMockData.secondaryMethods)
}

class PaymentFailedState {
    var amountDue by mutableStateOf(1240.00)
    var bookingName by mutableStateOf("The Grand Orion Suite")
    var bookingDetails by mutableStateOf("2 Nights • Oct 14 - Oct 16")
    var isRetrying by mutableStateOf(false)
    var errorMessage by mutableStateOf(
        "Your bank has declined this transaction. This can happen due to insufficient funds, card restrictions, or temporary security blocks."
    )
}