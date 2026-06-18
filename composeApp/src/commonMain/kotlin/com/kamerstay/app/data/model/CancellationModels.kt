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

// ── Refund tracking ───────────────────────────────────────────

enum class RefundStatus { INITIATED, PROCESSING, COMPLETED, FAILED }

data class RefundStep(
    val title: String,
    val subtitle: String,
    val date: String?,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class RefundTracking(
    val bookingId: String,
    val hotelName: String,
    val cancellationDate: String,
    val refundAmount: String,
    val paymentMethod: String,
    val estimatedArrival: String,
    val status: RefundStatus,
    val steps: List<RefundStep>
)