package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.mock.MapMockData
import com.kamerstay.app.data.model.CameroonLandmark
import com.kamerstay.app.data.model.LandmarkType
import com.kamerstay.app.data.model.MapHotel
import kotlin.math.*

class MapState {
    var searchText         by mutableStateOf("")
    var selectedHotelId    by mutableStateOf("d1")
    var selectedCity       by mutableStateOf("douala")
    var selectedLandmarkId by mutableStateOf<String?>(null)
    var userLat            by mutableStateOf<Double?>(null)
    var userLng            by mutableStateOf<Double?>(null)
    var locationGranted    by mutableStateOf(false)
    var isLoading          by mutableStateOf(false)

    val selectedLandmark: CameroonLandmark?
        get() = selectedLandmarkId?.let { id -> MapMockData.landmarks.find { it.id == id } }

    // Détecter la ville la plus proche des coords GPS
    fun detectCityFromPosition(lat: Double, lng: Double): String =
        MapMockData.cities.entries
            .minByOrNull { (_, c) -> haversineKm(lat, lng, c.lat, c.lng) }
            ?.key ?: selectedCity

    val filteredHotels: List<MapHotel>
        get() {
            val q  = searchText.trim().lowercase()
            val lm = selectedLandmark
            val cityLandmarks = MapMockData.landmarks.filter { it.city == selectedCity }

            return MapMockData.hotels
                .filter { h ->
                    h.city == selectedCity &&
                    (q.isEmpty() ||
                     h.name.lowercase().contains(q) ||
                     h.location.lowercase().contains(q) ||
                     h.amenities.any { it.lowercase().contains(q) } ||
                     cityLandmarks.filter { it.id in h.nearbyLandmarks }
                         .any { it.name.lowercase().contains(q) || it.displayName.lowercase().contains(q) })
                }
                .let { list ->
                    when {
                        lm != null -> list.sortedBy { haversineKm(it.lat, it.lng, lm.lat, lm.lng) }
                        userLat != null && userLng != null ->
                            list.sortedBy { haversineKm(it.lat, it.lng, userLat!!, userLng!!) }
                        else -> list
                    }
                }
        }

    // Distance en km vers le repère sélectionné ou la position utilisateur
    fun distanceKm(hotelLat: Double, hotelLng: Double): Double? {
        val lm = selectedLandmark
        return when {
            lm != null -> haversineKm(hotelLat, hotelLng, lm.lat, lm.lng)
            userLat != null && userLng != null -> haversineKm(hotelLat, hotelLng, userLat!!, userLng!!)
            else -> null
        }
    }

    // Distance uniquement depuis la position utilisateur (pour les cards)
    fun distanceFromUser(hotelLat: Double, hotelLng: Double): Double? {
        val lat = userLat ?: return null
        val lng = userLng ?: return null
        return haversineKm(hotelLat, hotelLng, lat, lng)
    }

    // Label formaté distance
    fun distanceLabel(hotelLat: Double, hotelLng: Double): String {
        val d = distanceKm(hotelLat, hotelLng) ?: return ""
        return if (d < 1.0) "${(d * 1000).toInt()} m" else "${"%.1f".format(d)} km"
    }

    // Temps de trajet estimé en voiture depuis la position utilisateur
    fun travelTimeLabel(hotelLat: Double, hotelLng: Double): String {
        val d = distanceFromUser(hotelLat, hotelLng) ?: return ""
        return when {
            d < 0.35 -> {
                val min = ceil(d / 4.0 * 60).toInt().coerceAtLeast(1)
                "~$min min à pied"
            }
            else -> {
                // Vitesse moyenne en ville camerounaise (trafic) : ~20 km/h
                val min = ceil(d / 20.0 * 60).toInt()
                if (min >= 60) "~${min / 60}h${if (min % 60 > 0) "${min % 60}min" else ""} en voiture"
                else "~$min min en voiture"
            }
        }
    }

    // Fix 1 — Applique les critères de FilterState sur les hôtels déjà filtrés par ville/recherche/repère
    fun applyFilters(filterState: FilterState): List<MapHotel> =
        filteredHotels.filter { h ->
            filterState.hotelMatches(h.priceXaf, h.rating, h.amenities)
        }

    // Fix 5 — Sélectionne sur la carte le repère correspondant au filtre FilterScreen
    fun syncLandmarkFromFilter(filterLandmark: String) {
        if (filterLandmark.isEmpty()) return
        val targetType = when (filterLandmark) {
            "Airport"     -> LandmarkType.AIRPORT
            "City Center" -> LandmarkType.MAIRIE
            else          -> null
        }
        selectedLandmarkId = MapMockData.landmarks
            .firstOrNull { it.city == selectedCity && it.type == (targetType ?: LandmarkType.QUARTIER) }
            ?.id
            ?: if (targetType == LandmarkType.MAIRIE)
                MapMockData.landmarks.firstOrNull { it.city == selectedCity && it.type == LandmarkType.QUARTIER }?.id
            else null
    }

    // Contexte affiché sous la distance
    fun distanceContext(): String = when {
        selectedLandmark != null -> selectedLandmark!!.displayName
            .removePrefix("à côté du ").removePrefix("à côté de la ")
            .removePrefix("près du ").removePrefix("près de la ").removePrefix("près de l'")
            .removePrefix("en plein cœur d'").removePrefix("dans le quartier ")
            .removePrefix("au ").removePrefix("à ")
        userLat != null -> "de vous"
        else -> ""
    }
}

private fun haversineKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val R = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLng / 2).pow(2)
    return R * 2 * atan2(sqrt(a), sqrt(1 - a))
}