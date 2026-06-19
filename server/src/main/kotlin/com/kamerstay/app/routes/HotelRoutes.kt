package com.kamerstay.app.routes

import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.repository.HotelRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.hotelRoutes(hotelRepository: HotelRepository) {

    route("/hotels") {

        get {
            val city = call.request.queryParameters["city"]
            val hotels = if (city != null) {
                hotelRepository.getHotelsByCity(city)
            } else {
                hotelRepository.getAllHotels()
            }
            call.respond(HttpStatusCode.OK, hotels)
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID manquant"))
                return@get
            }

            val hotel = hotelRepository.getHotelById(id)
            if (hotel == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Hotel introuvable"))
            } else {
                call.respond(HttpStatusCode.OK, hotel)
            }
        }
    }
}