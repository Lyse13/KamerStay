package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.TravelerCard
import com.kamerstay.app.data.model.TravelerCardType

object TravelerPaymentMockData {

    val primaryCard = TravelerCard(
        id = "1",
        label = "Visa Platinum",
        number = "4242",
        expiry = "09/27",
        type = TravelerCardType.VISA,
        isPrimary = true
    )

    val savedCards = listOf(
        TravelerCard(
            id = "1",
            label = "Visa Platinum",
            number = "4242",
            expiry = "09/27",
            type = TravelerCardType.VISA,
            isPrimary = true
        ),
        TravelerCard(
            id = "2",
            label = "MTN Mobile Money",
            number = "+237 677 890 123",
            expiry = "—",
            type = TravelerCardType.MOBILE_MONEY_MTN
        ),
        TravelerCard(
            id = "3",
            label = "Orange Money",
            number = "+237 699 456 789",
            expiry = "—",
            type = TravelerCardType.MOBILE_MONEY_ORANGE
        )
    )
}