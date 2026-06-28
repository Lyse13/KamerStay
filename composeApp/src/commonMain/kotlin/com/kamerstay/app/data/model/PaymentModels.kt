package com.kamerstay.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentInitRequest(
    val amount: Double,
    val phone: String,
    val operator: String,
    val description: String
)

@Serializable
data class PaymentInitResponse(
    val reference: String,
    val status: String,
    val operator: String = "",
    val message: String = ""
)

@Serializable
data class PaymentStatusResponse(
    val reference: String,
    val status: String,
    val operator: String = "",
    val amount: String = "",
    val currency: String = "XAF"
)