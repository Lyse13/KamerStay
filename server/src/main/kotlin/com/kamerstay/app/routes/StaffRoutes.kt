package com.kamerstay.app.routes

import com.kamerstay.app.model.Staff
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.repository.StaffRepository
import io.ktor.http.HttpStatusCode
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

fun Route.staffRoutes(staffRepository: StaffRepository) {

    route("/staff") {

        authenticate("auth-jwt") {

            // GET /staff?hotelId=xxx
            get {
                val hotelId = call.request.queryParameters["hotelId"]
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest, ErrorResponse("hotelId requis")
                    )
                call.respond(HttpStatusCode.OK, staffRepository.getStaffByHotel(hotelId))
            }

            // POST /staff
            post {
                val principal = call.principal<JWTPrincipal>()
                principal?.payload?.getClaim("userId")?.asString()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié")
                    )

                val req = call.receive<Staff>()
                if (req.fullName.isBlank() || req.hotelId.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Nom et hotelId requis"))
                    return@post
                }
                val member = req.copy(
                    id        = UUID.randomUUID().toString(),
                    status    = "ACTIVE",
                    createdAt = Clock.System.now().toString()
                )
                staffRepository.create(member)
                call.respond(HttpStatusCode.Created, member)
            }

            // PUT /staff/{id}
            put("/{id}") {
                val id = call.parameters["id"]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ErrorResponse("id manquant"))
                val req = call.receive<Staff>()
                val updated = staffRepository.update(req.copy(id = id))
                if (updated) call.respond(HttpStatusCode.OK, req.copy(id = id))
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("Membre introuvable"))
            }

            // DELETE /staff/{id}
            delete("/{id}") {
                val id = call.parameters["id"]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("id manquant"))
                val deleted = staffRepository.delete(id)
                if (deleted) call.respond(HttpStatusCode.OK, mapOf("message" to "Membre supprimé"))
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("Membre introuvable"))
            }
        }
    }
}