package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.kamerstay.app.data.mock.WishlistMockData
import com.kamerstay.app.data.model.SearchHotelResult
import com.kamerstay.app.data.model.WishlistHotel
import com.kamerstay.app.model.Hotel
import kotlin.math.absoluteValue

class WishlistState {
    var hotels by mutableStateOf<List<WishlistHotel>>(WishlistMockData.wishlistHotels)
    var isLoading by mutableStateOf(false)

    fun isInWishlist(hotelId: String) = hotels.any { it.id == hotelId }

    fun toggleFromHotel(hotel: Hotel) {
        if (isInWishlist(hotel.id)) {
            hotels = hotels.filter { it.id != hotel.id }
        } else {
            hotels = hotels + WishlistHotel(
                id = hotel.id,
                name = hotel.name,
                location = hotel.city,
                region = hotel.city,
                rating = hotel.rating,
                pricePerNight = hotel.pricePerNight.toInt(),
                isFavorite = true,
                gradientColors = defaultGradientForId(hotel.id)
            )
        }
    }

    fun toggleFromSearchResult(hotel: SearchHotelResult) {
        if (isInWishlist(hotel.id)) {
            hotels = hotels.filter { it.id != hotel.id }
        } else {
            hotels = hotels + WishlistHotel(
                id = hotel.id,
                name = hotel.name,
                location = hotel.location,
                region = hotel.district,
                rating = hotel.rating,
                pricePerNight = hotel.pricePerNight,
                isFavorite = true,
                gradientColors = hotel.gradientColors
            )
        }
    }

    fun toggleFavorite(hotelId: String) {
        hotels = hotels.map { hotel ->
            if (hotel.id == hotelId) hotel.copy(isFavorite = !hotel.isFavorite)
            else hotel
        }
    }

    fun removeFromWishlist(hotelId: String) {
        hotels = hotels.filter { it.id != hotelId }
    }

    val favoriteCount get() = hotels.count { it.isFavorite }

    private fun defaultGradientForId(id: String): List<Color> {
        val gradients = listOf(
            listOf(Color(0xFF0D4A6A), Color(0xFF1A2A3A)),
            listOf(Color(0xFF1A3A2E), Color(0xFF0D2218)),
            listOf(Color(0xFF2A1A3A), Color(0xFF1A0D28)),
            listOf(Color(0xFF3A2A1A), Color(0xFF281A0D))
        )
        return gradients[id.hashCode().absoluteValue % gradients.size]
    }
}