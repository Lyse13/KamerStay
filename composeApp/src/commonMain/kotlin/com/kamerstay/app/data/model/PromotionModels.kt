package com.kamerstay.app.data.model

enum class PromotionType(val label: String) {
    PROMO_CODE("Promo Code"),
    SEASONAL("Seasonal Rate"),
    EARLY_BIRD("Early Bird"),
    LAST_MINUTE("Last Minute")
}

enum class DiscountType(val label: String) {
    PERCENTAGE("Percentage"),
    FIXED_AMOUNT("Fixed Amount")
}

enum class PromotionStatus { ACTIVE, SCHEDULED, EXPIRED }

data class Promotion(
    val id: String,
    val code: String,
    val name: String,
    val type: PromotionType,
    val discountType: DiscountType,
    val discountValue: Int,
    val startDate: String,
    val endDate: String,
    val maxUsages: Int?,
    val usedCount: Int,
    val appliesTo: String,
    val status: PromotionStatus,
    val isActive: Boolean
) {
    val discountLabel: String get() = when (discountType) {
        DiscountType.PERCENTAGE -> "$discountValue% OFF"
        DiscountType.FIXED_AMOUNT -> "XAF ${"%,d".format(discountValue)} OFF"
    }

    val usageLabel: String get() = when {
        maxUsages == null -> "$usedCount used · Unlimited"
        else -> "$usedCount / $maxUsages used"
    }

    val usageRatio: Float get() = when {
        maxUsages == null || maxUsages == 0 -> 0f
        else -> (usedCount.toFloat() / maxUsages).coerceIn(0f, 1f)
    }
}