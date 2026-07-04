package com.kamerstay.app.routes

import com.kamerstay.app.model.HotelWithDistance
import com.kamerstay.app.repository.HotelRepository
import com.kamerstay.app.repository.LandmarkRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.math.*

// Calcul de distance Haversine entre deux points GPS (en km)
private fun haversineKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371.0 // rayon de la Terre en km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return earthRadius * c
}

// Estime le temps de trajet en voiture en milieu urbain camerounais.
// Ajoute un overhead fixe (sortie bâtiment, feux, stationnement) + vitesse selon distance :
//   < 1 km : 15 km/h (rues étroites, piétons, tuk-tuks)
//   1-5 km : 20 km/h (trafic urbain normal)
//   > 5 km : 30 km/h (axes principaux)
private fun estimateMinutes(distanceKm: Double): Int {
    val overheadMin = 3
    val speedKmh = when {
        distanceKm < 1.0 -> 15.0
        distanceKm < 5.0 -> 20.0
        else             -> 30.0
    }
    return (overheadMin + distanceKm / speedKmh * 60).roundToInt()
}

fun Route.landmarkRoutes(
    landmarkRepository: LandmarkRepository,
    hotelRepository: HotelRepository
) {
    // Liste de tous les landmarks
    get("/landmarks") {
        call.respond(HttpStatusCode.OK, landmarkRepository.getAllLandmarks())
    }

    // Landmarks d'une ville
    get("/landmarks/city/{city}") {
        val city = call.parameters["city"] ?: ""
        call.respond(HttpStatusCode.OK, landmarkRepository.getLandmarksByCity(city))
    }

    // Hôtels proches d'un landmark, triés par distance
    get("/hotels/near-landmark/{landmarkId}") {
        val landmarkId = call.parameters["landmarkId"]
            ?: return@get call.respond(HttpStatusCode.BadRequest, "landmarkId manquant")

        val landmark = landmarkRepository.getLandmarkById(landmarkId)
            ?: return@get call.respond(HttpStatusCode.NotFound, "Landmark introuvable")

        val allHotels = hotelRepository.getAllHotels()

        val result = allHotels
            .filter { it.latitude != 0.0 && it.longitude != 0.0 }
            .map { hotel ->
                val dist = haversineKm(
                    landmark.latitude, landmark.longitude,
                    hotel.latitude, hotel.longitude
                )
                HotelWithDistance(
                    hotel = hotel,
                    distanceKm = (dist * 10).roundToInt() / 10.0, // arrondi 1 décimale
                    estimatedMinutes = estimateMinutes(dist)
                )
            }
            .sortedBy { it.distanceKm }

        call.respond(HttpStatusCode.OK, result)
    }
}