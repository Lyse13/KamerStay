package com.kamerstay.app.model

import com.kamerstay.app.model.enums.BookingStatus
import com.kamerstay.app.model.enums.PaymentMethod
import com.kamerstay.app.model.enums.PaymentStatus
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val id: String = "",
    val travelerId: String = "",
    val hotelId: String = "",
    val roomId: String = "",
    val hotel: Hotel? = null,
    val room: Room? = null,
    val checkInDate: String = "",
    val checkOutDate: String = "",
    val numberOfNights: Int = 0,
    val totalAmount: Double = 0.0,
    val depositAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val paymentMethod: PaymentMethod? = null,
    val bookingStatus: BookingStatus = BookingStatus.PENDING,
    val specialRequests: String = "",
    val bookingReference: String = "",
    val createdAt: String = "",
    val confirmedAt: String? = null,
    val checkedInAt: String? = null,
    val checkedOutAt: String? = null
)
