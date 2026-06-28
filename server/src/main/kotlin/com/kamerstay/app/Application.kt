package com.kamerstay.app

import com.kamerstay.app.config.JwtConfig
import com.kamerstay.app.repository.BookingRepository
import com.kamerstay.app.repository.HotelRepository
import com.kamerstay.app.repository.LandmarkRepository
import com.kamerstay.app.repository.RoomRepository
import com.kamerstay.app.repository.UserRepository
import com.kamerstay.app.routes.aiRoutes
import com.kamerstay.app.routes.authRoutes
import com.kamerstay.app.routes.bookingRoutes
import com.kamerstay.app.routes.hotelRoutes
import com.kamerstay.app.repository.ReviewRepository
import com.kamerstay.app.repository.StaffRepository
import com.kamerstay.app.routes.paymentRoutes
import com.kamerstay.app.routes.reviewRoutes
import com.kamerstay.app.routes.staffRoutes
import com.kamerstay.app.util.SeedData
import com.kamerstay.app.routes.landmarkRoutes
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

const val SERVER_PORT = 8080

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val userRepository    = UserRepository()
    val hotelRepository   = HotelRepository()
    val roomRepository    = RoomRepository()
    val bookingRepository = BookingRepository()
    val landmarkRepository = LandmarkRepository()
    val reviewRepository  = ReviewRepository()
    val staffRepository   = StaffRepository()

    SeedData.seedIfEmpty(hotelRepository)
    SeedData.seedRoomsIfEmpty(roomRepository)
    SeedData.seedAdminIfNotExists(userRepository)

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }

    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowHeader("Authorization")
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                com.auth0.jwt.JWT
                    .require(JwtConfig.algorithm)
                    .withIssuer(JwtConfig.issuer)
                    .withAudience(JwtConfig.audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString() != null) {
                    JWTPrincipal(credential.payload)
                } else null
            }
        }
    }

    routing {
        authRoutes(userRepository)
        hotelRoutes(hotelRepository, roomRepository)
        bookingRoutes(bookingRepository, hotelRepository)
        paymentRoutes()
        landmarkRoutes(landmarkRepository, hotelRepository)
        reviewRoutes(reviewRepository)
        staffRoutes(staffRepository)
        aiRoutes()
    }
}