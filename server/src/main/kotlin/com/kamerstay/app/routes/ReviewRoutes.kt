package com.kamerstay.app.routes

import com.kamerstay.app.model.Review
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.repository.ReviewRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlin.time.Clock
import java.util.UUID

fun Route.reviewRoutes(reviewRepository: ReviewRepository) {

    route("/reviews") {

        // Récupérer les avis d'un hôtel (public)
        get("/hotel/{hotelId}") {
            val hotelId = call.parameters["hotelId"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("hotelId manquant")
                )
            val reviews = reviewRepository.getReviewsByHotel(hotelId)
            call.respond(HttpStatusCode.OK, reviews)
        }

        // Soumettre un avis (authentifié)
        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val travelerId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ErrorResponse("Non authentifié")
                    )
                val travelerName = principal.payload.getClaim("fullName")?.asString() ?: "Voyageur"

                val request = call.receive<Review>()

                if (request.hotelId.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("hotelId requis"))
                    return@post
                }
                if (request.rating < 1.0 || request.rating > 5.0) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Note entre 1 et 5"))
                    return@post
                }

                val review = request.copy(
                    id           = UUID.randomUUID().toString(),
                    travelerId   = travelerId,
                    travelerName = travelerName.ifBlank { "Voyageur" },
                    createdAt    = Clock.System.now().toString()
                )

                reviewRepository.createReview(review)
                call.respond(HttpStatusCode.Created, review)
            }
        }
    }
}