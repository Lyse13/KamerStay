package com.kamerstay.app.routes

import com.kamerstay.app.config.JwtConfig
import com.kamerstay.app.model.User
import com.kamerstay.app.model.dto.AuthResponse
import com.kamerstay.app.util.EmailSender
import com.kamerstay.app.model.dto.ErrorResponse
import com.kamerstay.app.model.dto.LoginRequest
import com.kamerstay.app.model.dto.RegisterRequest
import com.kamerstay.app.model.enums.UserRole
import com.kamerstay.app.repository.UserRepository
import com.kamerstay.app.util.PasswordHasher
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlinx.serialization.Serializable

// ── OTP store en mémoire ──────────────────────────────────────
private data class OtpEntry(val code: String, val expiresAt: Long)
private val otpStore = ConcurrentHashMap<String, OtpEntry>()

// ── DTOs password reset ───────────────────────────────────────
@Serializable data class ForgotPasswordRequest(val email: String)
@Serializable data class ForgotPasswordResponse(
    val success: Boolean,
    val message: String,
    val demoCode: String = ""   // retiré quand SMS/email sera intégré
)
@Serializable data class VerifyCodeRequest(val email: String, val code: String)
@Serializable data class VerifyCodeResponse(
    val valid: Boolean,
    val resetToken: String = "",
    val message: String = ""
)
@Serializable data class ResetPasswordRequest(val resetToken: String, val newPassword: String)

@Serializable data class UpdateProfileRequest(val fullName: String, val phoneNumber: String)
@Serializable data class UpdateProfileResponse(val success: Boolean, val fullName: String, val phoneNumber: String)
@Serializable data class FcmTokenRequest(val token: String)
@Serializable data class ChangePasswordRequest(val currentPassword: String, val newPassword: String)

fun Route.authRoutes(
    userRepository: UserRepository,
    notificationRepository: com.kamerstay.app.repository.NotificationRepository = com.kamerstay.app.repository.NotificationRepository()
) {

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

            val role = when (request.role.uppercase()) {
                "HOTEL_MANAGER", "MANAGER" -> UserRole.HOTEL_MANAGER
                else -> UserRole.TRAVELER
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

            val token = JwtConfig.generateToken(newUser.id, newUser.email, newUser.role.name, newUser.fullName)
            call.respond(HttpStatusCode.Created, AuthResponse(token = token, user = newUser))
        }

        rateLimit(RateLimitName("auth")) {
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

            val token = JwtConfig.generateToken(user.id, user.email, user.role.name, user.fullName)
            call.respond(HttpStatusCode.OK, AuthResponse(token = token, user = user))
        }
        } // end rateLimit("auth") login

        // ── Mot de passe oublié : envoyer OTP ────────────────
        rateLimit(RateLimitName("auth")) {
        post("/forgot-password") {
            val req = call.receive<ForgotPasswordRequest>()
            if (req.email.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, ForgotPasswordResponse(false, "Email requis"))
                return@post
            }
            if (!userRepository.emailExists(req.email)) {
                // Réponse identique pour ne pas révéler si l'email existe
                call.respond(HttpStatusCode.OK, ForgotPasswordResponse(
                    success  = true,
                    message  = "Si cet email existe, un code a été envoyé.",
                    demoCode = ""
                ))
                return@post
            }
            val code = (1000..9999).random().toString()
            val expiry = System.currentTimeMillis() + 10 * 60 * 1000L  // 10 min
            otpStore[req.email] = OtpEntry(code, expiry)

            val emailSent = EmailSender.sendOtpEmail(req.email, code)
            call.respond(HttpStatusCode.OK, ForgotPasswordResponse(
                success  = true,
                message  = if (emailSent) "Code envoyé à votre adresse email." else "Code généré.",
                demoCode = ""
            ))
        }
        } // end rateLimit("auth") forgot-password

        // ── Vérifier le code OTP ──────────────────────────────
        post("/verify-code") {
            val req = call.receive<VerifyCodeRequest>()
            val entry = otpStore[req.email]
            when {
                entry == null -> call.respond(HttpStatusCode.BadRequest,
                    VerifyCodeResponse(false, message = "Code inexistant ou expiré."))
                System.currentTimeMillis() > entry.expiresAt -> {
                    otpStore.remove(req.email)
                    call.respond(HttpStatusCode.BadRequest,
                        VerifyCodeResponse(false, message = "Code expiré. Redemandez un code."))
                }
                entry.code != req.code -> call.respond(HttpStatusCode.BadRequest,
                    VerifyCodeResponse(false, message = "Code incorrect."))
                else -> {
                    otpStore.remove(req.email)  // usage unique
                    val resetToken = JwtConfig.generateResetToken(req.email)
                    call.respond(HttpStatusCode.OK, VerifyCodeResponse(
                        valid      = true,
                        resetToken = resetToken,
                        message    = "Code vérifié."
                    ))
                }
            }
        }

        // ── Mettre à jour le profil ───────────────────────────
        authenticate("auth-jwt") {
            put("/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@put call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié"))

                val req = call.receive<UpdateProfileRequest>()
                if (req.fullName.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Le nom ne peut pas être vide"))
                    return@put
                }
                userRepository.updateUser(userId, req.fullName.trim(), req.phoneNumber.trim())
                call.respond(HttpStatusCode.OK, UpdateProfileResponse(
                    success     = true,
                    fullName    = req.fullName.trim(),
                    phoneNumber = req.phoneNumber.trim()
                ))
            }
        }

        // ── Changer le mot de passe (utilisateur connecté) ───
        authenticate("auth-jwt") {
            put("/change-password") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@put call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié"))

                val req = call.receive<ChangePasswordRequest>()
                if (req.newPassword.length < 8) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Mot de passe trop court (8 caractères min.)"))
                    return@put
                }

                val user = userRepository.findUserById(userId)
                    ?: return@put call.respond(HttpStatusCode.NotFound, ErrorResponse("Utilisateur introuvable"))

                val credentials = userRepository.findCredentialsByEmail(user.email)
                    ?: return@put call.respond(HttpStatusCode.NotFound, ErrorResponse("Identifiants introuvables"))

                if (!PasswordHasher.verify(req.currentPassword, credentials.passwordHash)) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Mot de passe actuel incorrect"))
                    return@put
                }

                userRepository.updatePassword(user.email, PasswordHasher.hash(req.newPassword))
                call.respond(HttpStatusCode.OK, mapOf("success" to true, "message" to "Mot de passe mis à jour."))
            }
        }

        // ── Enregistrer le token FCM ──────────────────────────
        authenticate("auth-jwt") {
            put("/fcm-token") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asString()
                    ?: return@put call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Non authentifié"))
                val req = call.receive<FcmTokenRequest>()
                if (req.token.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Token FCM requis"))
                    return@put
                }
                notificationRepository.upsertToken(userId, req.token)
                call.respond(HttpStatusCode.OK, mapOf("success" to true))
            }
        }

        // ── Réinitialiser le mot de passe ─────────────────────
        post("/reset-password") {
            val req = call.receive<ResetPasswordRequest>()
            if (req.newPassword.length < 8) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Mot de passe trop court (8 caractères min.)"))
                return@post
            }
            val decoded = JwtConfig.verifyResetToken(req.resetToken)
            val email = decoded?.getClaim("email")?.asString()
            if (decoded == null || email.isNullOrBlank()) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Token invalide ou expiré."))
                return@post
            }
            userRepository.updatePassword(email, PasswordHasher.hash(req.newPassword))
            call.respond(HttpStatusCode.OK, mapOf("success" to true, "message" to "Mot de passe mis à jour."))
        }
    }
}