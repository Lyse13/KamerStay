package com.kamerstay.app.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val SECRET = "kamerstay-secret-key-changer-en-production-2026"
    private const val ISSUER = "kamerstay-server"
    private const val AUDIENCE = "kamerstay-app"
    private const val VALIDITY_MS = 3_600_000 * 24   // 24 heures en millisecondes

    val algorithm: Algorithm = Algorithm.HMAC256(SECRET)
    const val issuer = ISSUER
    const val audience = AUDIENCE

    fun generateResetToken(email: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("email", email)
            .withClaim("purpose", "password_reset")
            .withExpiresAt(Date(System.currentTimeMillis() + 15 * 60 * 1000))
            .sign(algorithm)
    }

    fun verifyResetToken(token: String): com.auth0.jwt.interfaces.DecodedJWT? {
        return try {
            JWT.require(algorithm).withIssuer(ISSUER).withAudience(AUDIENCE)
                .build().verify(token)
                .takeIf { it.getClaim("purpose").asString() == "password_reset" }
        } catch (_: Exception) { null }
    }

    fun generateToken(userId: String, email: String, role: String, fullName: String = ""): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withClaim("fullName", fullName)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_MS))
            .sign(algorithm)
    }
}