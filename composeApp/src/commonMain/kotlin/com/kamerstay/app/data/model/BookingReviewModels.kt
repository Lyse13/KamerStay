package com.kamerstay.app.data.model

data class BookingReviewData(
    val hotelName: String,
    val location: String,
    val rating: Double,
    val amenities: List<String>,
    val checkIn: String,
    val checkOut: String,
    val nights: Int,
    val guests: Int,
    val rooms: Int,
    val roomType: String,
    val roomDetail: String,
    val pricePerNight: Double,
    val serviceFee: Double,
    val taxesFees: Double
) {
    val roomTotal get() = pricePerNight * nights
    val total get() = roomTotal + serviceFee + taxesFees
}