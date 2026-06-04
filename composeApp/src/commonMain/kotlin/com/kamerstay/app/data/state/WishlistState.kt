package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.WishlistHotel
import com.kamerstay.app.data.mock.WishlistMockData

class WishlistState {
    var hotels by mutableStateOf<List<WishlistHotel>>(WishlistMockData.wishlistHotels)
    var isLoading by mutableStateOf(false)

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
}