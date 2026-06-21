package com.kamerstay.app.routes

import com.kamerstay.app.model.Booking
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.model.enums.BookingStatus
import com.kamerstay.app.model.enums.PaymentStatus
import com.kamerstay.app.repository.BookingRepository
import com.kamerstay.app.repository.HotelRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import kotlin.time.Clock
import java.util.UUID

fun Route.bookingRoutes(
    bookingRepository: BookingRepository,
    hotelRepository: HotelRepository
) {
    route("/bookings") {

        // Créer une réservation (authentifié)
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val travelerId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse("Non authentifié")
                    )

                val request = call.receive<Booking>()

                // Récupérer l'hôtel pour l'inclure dans la réservation
                val hotel = hotelRepository.getHotelById(request.hotelId)

                val booking = request.copy(
                    id = UUID.randomUUID().toString(),
                    travelerId = travelerId,
                    hotel = hotel,
                    bookingReference = "KS-${System.currentTimeMillis().toString().takeLast(8)}",
                    bookingStatus = BookingStatus.CONFIRMED,
                    paymentStatus = PaymentStatus.DEPOSIT_PAID,
                    createdAt = Clock.System.now().toString(),
                    confirmedAt = Clock.System.now().toString()
                )

                bookingRepository.createBooking(booking)
                call.respond(HttpStatusCode.Created, booking)
            }

            // Toutes les réservations (manager)
            get("/all") {
                call.principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString()
                    ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse("Non authentifié")
                    )
                val bookings = bookingRepository.getAllBookings()
                call.respond(HttpStatusCode.OK, bookings)
            }

            // Mes réservations (voyageur connecté)
            get("/my") {
                val principal = call.principal<JWTPrincipal>()
                val travelerId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse("Non authentifié")
                    )

                val bookings = bookingRepository.getBookingsByTraveler(travelerId)
                call.respond(HttpStatusCode.OK, bookings)
            }

            // Réservations d'un hôtel (manager)
            get("/hotel/{hotelId}") {
                val hotelId = call.parameters["hotelId"]
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("hotelId manquant")
                    )
                val bookings = bookingRepository.getBookingsByHotel(hotelId)
                call.respond(HttpStatusCode.OK, bookings)
            }

            // Mettre à jour le statut (check-in, check-out, annulation)
            put("/{id}/status") {
                val id = call.parameters["id"]
                    ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("id manquant")
                    )
                val body = call.receive<Map<String, String>>()
                val status = body["status"]
                    ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("status manquant")
                    )

                val updated = bookingRepository.updateBookingStatus(id, status)
                if (updated) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Statut mis à jour"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Réservation introuvable"))
                }
            }
        }
    }
}