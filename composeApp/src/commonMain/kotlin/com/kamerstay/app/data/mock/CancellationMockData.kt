package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.CancellationPolicy
import com.kamerstay.app.data.model.CancellationPolicyType
import com.kamerstay.app.data.model.CancellationSummary
import com.kamerstay.app.data.model.RefundBreakdown

object CancellationMockData {

    val summary = CancellationSummary(
        bookingId = "BK-20241234",
        hotelName = "The Azure Vista",
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
}