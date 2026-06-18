package com.kamerstay.app.data.mock

import com.kamerstay.app.data.model.DiscountType
import com.kamerstay.app.data.model.Promotion
import com.kamerstay.app.data.model.PromotionStatus
import com.kamerstay.app.data.model.PromotionType

object PromotionsMockData {

    val promotions = mutableListOf(
        // ── Active ────────────────────────────────────────
        Promotion(
            id = "p1",
            code = "SUMMER20",
            name = "Summer Special",
            type = PromotionType.SEASONAL,
            discountType = DiscountType.PERCENTAGE,
            discountValue = 20,
            startDate = "Jul 1, 2024",
            endDate = "Sep 30, 2024",
            maxUsages = 50,
            usedCount = 32,
            appliesTo = "All Rooms",
            status = PromotionStatus.ACTIVE,
            isActive = true
        ),
        Promotion(
            id = "p2",
            code = "LASTMIN10",
            name = "Last Minute Deal",
            type = PromotionType.LAST_MINUTE,
            discountType = DiscountType.PERCENTAGE,
            discountValue = 10,
            startDate = "Oct 1, 2024",
            endDate = "Dec 31, 2024",
            maxUsages = null,
            usedCount = 14,
            appliesTo = "All Rooms",
            status = PromotionStatus.ACTIVE,
            isActive = true
        ),
        // ── Scheduled ─────────────────────────────────────
        Promotion(
            id = "p3",
            code = "EARLYBIRD15",
            name = "Early Bird 30-Day",
            type = PromotionType.EARLY_BIRD,
            discountType = DiscountType.PERCENTAGE,
            discountValue = 15,
            startDate = "Nov 1, 2024",
            endDate = "Dec 31, 2024",
            maxUsages = 100,
            usedCount = 0,
            appliesTo = "All Rooms",
            status = PromotionStatus.SCHEDULED,
            isActive = false
        ),
        Promotion(
            id = "p4",
            code = "XMAS2024",
            name = "Christmas Holiday",
            type = PromotionType.SEASONAL,
            discountType = DiscountType.PERCENTAGE,
            discountValue = 25,
            startDate = "Dec 20, 2024",
            endDate = "Dec 31, 2024",
            maxUsages = null,
            usedCount = 0,
            appliesTo = "All Rooms",
            status = PromotionStatus.SCHEDULED,
            isActive = false
        ),
        // ── Expired ───────────────────────────────────────
        Promotion(
            id = "p5",
            code = "SUITE2024",
            name = "Penthouse August",
            type = PromotionType.PROMO_CODE,
            discountType = DiscountType.FIXED_AMOUNT,
            discountValue = 50000,
            startDate = "Aug 1, 2024",
            endDate = "Aug 31, 2024",
            maxUsages = 20,
            usedCount = 20,
            appliesTo = "Penthouse",
            status = PromotionStatus.EXPIRED,
            isActive = false
        ),
        Promotion(
            id = "p6",
            code = "WEEKEND5",
            name = "Weekend Escape",
            type = PromotionType.PROMO_CODE,
            discountType = DiscountType.PERCENTAGE,
            discountValue = 5,
            startDate = "May 1, 2024",
            endDate = "Jun 30, 2024",
            maxUsages = null,
            usedCount = 47,
            appliesTo = "All Rooms",
            status = PromotionStatus.EXPIRED,
            isActive = false
        )
    )

    fun getById(id: String) = promotions.find { it.id == id }
}