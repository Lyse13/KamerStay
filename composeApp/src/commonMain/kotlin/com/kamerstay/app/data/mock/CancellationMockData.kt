package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.CancellationPolicy
import com.kamerstay.app.data.model.CancellationPolicyType
import com.kamerstay.app.data.model.CancellationSummary
import com.kamerstay.app.data.model.RefundBreakdown
import com.kamerstay.app.data.model.RefundStatus
import com.kamerstay.app.data.model.RefundStep
import com.kamerstay.app.data.model.RefundTracking

object CancellationMockData {

    val summary = CancellationSummary(
        bookingId = "BK-20241234",
        hotelName = "Sawa Hôtel & SPA",
        roomType = "Deluxe Suite",
        checkIn = "Nov 15, 2024",
        checkOut = "Nov 20, 2024",
        guests = "2 Adults",
        totalPaid = "XAF 450,000"
    )

    val policies = listOf(
        CancellationPolicy(
            id = "1",
            description = "Free cancellation before Nov 10, 2024",
            type = CancellationPolicyType.FREE
        ),
        CancellationPolicy(
            id = "2",
            description = "50% fee if cancelled between Nov 10 – Nov 13",
            type = CancellationPolicyType.PARTIAL
        ),
        CancellationPolicy(
            id = "3",
            description = "No refund if cancelled within 48h of check-in",
            type = CancellationPolicyType.NO_REFUND
        )
    )

    val refund = RefundBreakdown(
        totalPaid = "XAF 450,000",
        cancellationFee = "- XAF 225,000",
        feeLabel = "Cancellation fee (50%)",
        estimatedRefund = "XAF 225,000",
        refundNote = "Refund will be processed within 5-7 business days to your original payment method."
    )

    val refundTracking = RefundTracking(
        bookingId = "BK-20241234",
        hotelName = "Sawa Hôtel & SPA",
        cancellationDate = "Nov 14, 2024",
        refundAmount = "XAF 225,000",
        paymentMethod = "MTN Mobile Money ••••7821",
        estimatedArrival = "Nov 19 – Nov 21, 2024",
        status = RefundStatus.PROCESSING,
        steps = listOf(
            RefundStep(
                title = "Cancellation Confirmed",
                subtitle = "Your booking has been successfully cancelled.",
                date = "Nov 14, 2024 • 10:32 AM",
                isCompleted = true,
                isCurrent = false
            ),
            RefundStep(
                title = "Refund Initiated",
                subtitle = "Refund request sent to payment provider.",
                date = "Nov 14, 2024 • 10:35 AM",
                isCompleted = true,
                isCurrent = false
            ),
            RefundStep(
                title = "Refund Processing",
                subtitle = "Your refund is being processed by MTN Mobile Money.",
                date = "Est. Nov 15 – Nov 19, 2024",
                isCompleted = false,
                isCurrent = true
            ),
            RefundStep(
                title = "Refunded to Account",
                subtitle = "Funds will appear in your account.",
                date = null,
                isCompleted = false,
                isCurrent = false
            )
        )
    )
}