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
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.time.Duration.Companion.minutes
import kotlinx.serialization.json.Json

const val SERVER_PORT = 8080

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        registry = prometheusRegistry
        meterBinders = listOf(
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            JvmThreadMetrics(),
            ClassLoaderMetrics(),
            ProcessorMetrics()
        )
    }

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

    install(RateLimit) {
        // 5 tentatives par minute par IP sur les routes d'auth sensibles
        register(RateLimitName("auth")) {
            rateLimiter(limit = 5, refillPeriod = 1.minutes)
            requestKey { call -> call.request.local.remoteAddress }
        }
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
        get("/") { call.respond(HttpStatusCode.OK, "KamerStay API") }
        get("/metrics") { call.respondText(prometheusRegistry.scrape(), ContentType.Text.Plain) }
        authRoutes(userRepository)
        hotelRoutes(hotelRepository, roomRepository)
        bookingRoutes(bookingRepository, hotelRepository)
        paymentRoutes()
        landmarkRoutes(landmarkRepository, hotelRepository)
        reviewRoutes(reviewRepository)
        staffRoutes(staffRepository)
        aiRoutes(hotelRepository)
    }
}