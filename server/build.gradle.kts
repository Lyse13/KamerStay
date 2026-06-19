plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.kamerstay.app"
version = "1.0.0"

application {
    mainClass = "com.kamerstay.app.ApplicationKt"
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

dependencies {
    // Shared module
    implementation(project(":shared"))

    // Ktor Server
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.authJwt)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.serialization)

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