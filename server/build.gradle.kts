import java.util.Properties

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.kamerstay.app"
version = "1.0.0"

val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) load(f.inputStream())
}

application {
    mainClass = "com.kamerstay.app.ApplicationKt"
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

tasks.named<JavaExec>("run") {
    environment("ANTHROPIC_API_KEY", localProps.getProperty("ANTHROPIC_API_KEY") ?: "")
    environment("RESEND_API_KEY",    localProps.getProperty("RESEND_API_KEY")    ?: "")
    environment("RESEND_FROM_EMAIL", localProps.getProperty("RESEND_FROM_EMAIL") ?: "")
    environment("JWT_SECRET",        localProps.getProperty("JWT_SECRET")        ?: "")
    environment("DEV_MODE",          localProps.getProperty("DEV_MODE")          ?: "true")
}

dependencies {
    // Shared module
    implementation(project(":shared"))

    // Ktor Client (pour appels Anthropic API)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentNegotiation)

    // Ktor Server
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.authJwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.serialization)
    implementation(libs.ktor.server.rateLimit)
    implementation(libs.ktor.server.metrics.micrometer)
    implementation(libs.micrometer.registry.prometheus)

    // Koin
    implementation(libs.koin.ktor)

    // MongoDB
    implementation(libs.mongodb.driver.coroutine)
    implementation(libs.bcrypt)
    implementation(libs.java.jwt)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // DateTime
    implementation(libs.kotlinx.datetime)

    // Logback
    implementation(libs.logback)

    // Tests
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}