package com.kamerstay.app.routes

import com.kamerstay.app.model.Hotel
import com.kamerstay.app.model.Room
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.model.enums.RoomStatus
import com.kamerstay.app.repository.HotelRepository
import com.kamerstay.app.repository.RoomRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.util.UUID
import kotlin.time.Clock

fun Route.hotelRoutes(hotelRepository: HotelRepository, roomRepository: RoomRepository) {

    route("/hotels") {

        // ── GET /hotels ──────────────────────────────────────
        get {
            val city = call.request.queryParameters["city"]
            val hotels = if (city != null) {
                hotelRepository.getHotelsByCity(city)
            } else {
                hotelRepository.getAllHotels()
            }
            call.respond(HttpStatusCode.OK, hotels)
        }

        // ── GET /hotels/{id} ─────────────────────────────────
        get("/{id}") {
            val id = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID manquant"))
            val hotel = hotelRepository.getHotelById(id)
            if (hotel == null) call.respond(HttpStatusCode.NotFound, ErrorResponse("Hotel introuvable"))
            else call.respond(HttpStatusCode.OK, hotel)
        }

        // ── GET /hotels/{id}/rooms ───────────────────────────
        get("/{id}/rooms") {
            val id = call.parameters["id"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("ID manquant"))
            call.respond(HttpStatusCode.OK, roomRepository.getRoomsByHotelId(id))
        }

        // ── POST /hotels/{id}/rooms ──────────────────────────
        authenticate("auth-jwt") {
            post("/{id}/rooms") {
                val hotelId = call.parameters["id"]
                    ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("hotelId manquant"))
                val req = call.receive<Room>()
                if (req.roomNumber.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Numéro de chambre requis"))
                    return@post
                }
                val room = req.copy(
                    id      = UUID.randomUUID().toString(),
                    hotelId = hotelId,
                    status  = RoomStatus.AVAILABLE
                )
                roomRepository.insertRoom(room)
                call.respond(HttpStatusCode.Created, room)
            }

            // ── PUT /hotels/{id}/rooms/{roomId} ───────────────
            put("/{id}/rooms/{roomId}") {
                val roomId = call.parameters["roomId"]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ErrorResponse("roomId manquant"))
                val hotelId = call.parameters["id"] ?: ""
                val req = call.receive<Room>()
                val updated = roomRepository.updateRoom(req.copy(id = roomId, hotelId = hotelId))
                if (updated) call.respond(HttpStatusCode.OK, req.copy(id = roomId, hotelId = hotelId))
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("Chambre introuvable"))
            }

            // ── DELETE /hotels/{id}/rooms/{roomId} ────────────
            delete("/{id}/rooms/{roomId}") {
                val roomId = call.parameters["roomId"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("roomId manquant"))
                val deleted = roomRepository.deleteRoom(roomId)
                if (deleted) call.respond(HttpStatusCode.OK, mapOf("message" to "Chambre supprimée"))
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("Chambre introuvable"))
            }
        }

        // ── GET /hotels/my ───────────────────────────────────
        authenticate("auth-jwt") {
            get("/my") {
                val principal = call.principal<JWTPrincipal>()
                val managerId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié"))
                val hotel = hotelRepository.getHotelByManagerId(managerId)
                if (hotel == null) call.respond(HttpStatusCode.NotFound, ErrorResponse("Aucun hôtel enregistré"))
                else call.respond(HttpStatusCode.OK, hotel)
            }
        }

        // ── POST /hotels ─────────────────────────────────────
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val managerId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié")
                    )

                val req = call.receive<Hotel>()

                if (req.name.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Nom d'hôtel requis"))
                    return@post
                }
                if (req.city.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ville requise"))
                    return@post
                }

                val hotel = req.copy(
                    id         = UUID.randomUUID().toString(),
                    managerId  = managerId,
                    isVerified = false,
                    rating     = 0.0,
                    reviewCount = 0,
                    availableRooms = req.totalRooms,
                    createdAt  = Clock.System.now().toString()
                )

                hotelRepository.insertHotel(hotel)
                call.respond(HttpStatusCode.Created, hotel)
            }
        }
    }
}