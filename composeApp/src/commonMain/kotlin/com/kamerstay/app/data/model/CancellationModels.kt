package com.kamerstay.app.data.model

data class CancellationSummary(
    val bookingId: String,
    val hotelName: String,
    val roomType: String,
    val checkIn: String,
    val checkOut: String,
    val guests: String,
    val totalPaid: String
)

data class CancellationPolicy(
    val id: String,
    val description: String,
    val type: CancellationPolicyType
)

enum class CancellationPolicyType { FREE, PARTIAL, NO_REFUND }

data class RefundBreakdown(
    val totalPaid: String,
    val cancellationFee: String,
    val feeLabel: String,
    val estimatedRefund: String,
    val refundNote: String
)