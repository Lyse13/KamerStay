package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.mock.CancellationMockData
import com.kamerstay.app.data.model.CancellationPolicy
import com.kamerstay.app.data.model.CancellationSummary
import com.kamerstay.app.data.model.RefundBreakdown

class CancellationState {
    var summary by mutableStateOf<CancellationSummary>(CancellationMockData.summary)
    var policies by mutableStateOf<List<CancellationPolicy>>(CancellationMockData.policies)
    var refund by mutableStateOf<RefundBreakdown>(CancellationMockData.refund)
    var isConfirmed by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
}