package com.kamerstay.app.features.traveler

import androidx.compose.runtime.Composable

@Composable
expect fun HotelLocationMap(
    latitude: Double,
    longitude: Double,
    hotelName: String,
    onClick: () -> Unit
)