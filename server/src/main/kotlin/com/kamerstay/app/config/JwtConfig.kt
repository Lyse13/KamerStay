package com.kamerstay.app.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val SECRET = "kamerstay-secret-key-changer-en-production-2026"
    private const val ISSUER = "kamerstay-server"
    private const val AUDIENCE = "kamerstay-app"
    private const val VALIDITY_MS = 36_000_00 * 24

    val algorithm: Algorithm = Algorithm.HMAC256(SECRET)
    const val issuer = ISSUER
    const val audience = AUDIENCE

    fun generateToken(userId: String, email: String, role: String): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + VALIDITY_MS))
            .sign(algorithm)
    }
}