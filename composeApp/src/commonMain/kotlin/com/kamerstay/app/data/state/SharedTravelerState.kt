package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.model.Hotel

class SharedTravelerState {

    val filterState = FilterState()
    val searchState = SearchState()

    var hotels by mutableStateOf<List<Hotel>>(emptyList())
    var allHotels: List<Hotel> = emptyList()
    var isLoadingHotels by mutableStateOf(false)
    var hotelsError by mutableStateOf<String?>(null)

    var priceSortAscending by mutableStateOf<Boolean?>(null)

    val displayedHotels get() = when (priceSortAscending) {
        true  -> hotels.sortedBy { it.pricePerNight }
        false -> hotels.sortedByDescending { it.pricePerNight }
        null  -> hotels
    }

    fun togglePriceSort() {
        priceSortAscending = when (priceSortAscending) {
            null  -> true
            true  -> false
            false -> null
        }
    }

    fun searchHotels() {
        hotels = if (searchState.query.isEmpty()) {
            allHotels
        } else {
            val q = searchState.query.normalize()
            allHotels.filter {
                it.city.normalize().contains(q) ||
                it.name.normalize().contains(q)
            }
        }
    }
}

private fun String.normalize(): String =
    this.lowercase()
        .replace("é", "e").replace("è", "e").replace("ê", "e").replace("ë", "e")
        .replace("à", "a").replace("â", "a").replace("ä", "a")
        .replace("ô", "o").replace("ö", "o")
        .replace("û", "u").replace("ù", "u").replace("ü", "u")
        .replace("î", "i").replace("ï", "i")
        .replace("ç", "c")
