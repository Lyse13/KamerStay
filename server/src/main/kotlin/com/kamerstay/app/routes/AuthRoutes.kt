package com.kamerstay.app.routes

import com.kamerstay.app.config.JwtConfig
import com.kamerstay.app.model.User
import com.kamerstay.app.model.dto.AuthResponse
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.model.dto.LoginRequest
import com.kamerstay.app.model.dto.RegisterRequest
import com.kamerstay.app.model.enums.UserRole
import com.kamerstay.app.repository.UserRepository
import com.kamerstay.app.util.PasswordHasher
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.UUID

fun Route.authRoutes(userRepository: UserRepository) {

    route("/auth") {

        post("/register") {
            val request = call.receive<RegisterRequest>()

            if (request.email.isBlank() || request.password.length < 6) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorResponse("Email requis et mot de passe d'au moins 6 caracteres")
                )
                return@post
            }

            if (userRepository.emailExists(request.email)) {
                call.respond(
                    HttpStatusCode.Conflict,
                    ErrorResponse("Un compte existe deja avec cet email")
                )
                return@post
            }

            val role = try {
                UserRole.valueOf(request.role.uppercase())
            } catch (_: IllegalArgumentException) {
                UserRole.TRAVELER
            }

            val newUser = User(
                id = UUID.randomUUID().toString(),
                fullName = request.fullName,
                email = request.email,
                phoneNumber = request.phoneNumber,
                role = role,
                createdAt = java.time.Instant.now().toString(),
                isActive = true
            )

            val passwordHash = PasswordHasher.hash(request.password)
            userRepository.createUser(newUser, passwordHash)

            val token = JwtConfig.generateToken(newUser.id, newUser.email, newUser.role.name)
            call.respond(HttpStatusCode.Created, AuthResponse(token = token, user = newUser))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()

            val credentials = userRepository.findCredentialsByEmail(request.email)
            if (credentials == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Email ou mot de passe incorrect"))
                return@post
            }

            val isValid = PasswordHasher.verify(request.password, credentials.passwordHash)
            if (!isValid) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Email ou mot de passe incorrect"))
                return@post
            }

            val user = userRepository.findUserById(credentials.userId)
            if (user == null) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Utilisateur introuvable"))
                return@post
            }

            val token = JwtConfig.generateToken(user.id, user.email, user.role.name)
            call.respond(HttpStatusCode.OK, AuthResponse(token = token, user = user))
        }
    }
}