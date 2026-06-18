package com.kamerstay.app.core.utils

import kotlinx.datetime.*

fun String.toLocalDate(): LocalDate? = try {
    LocalDate.parse(this)
} catch (e: Exception) { null }

fun LocalDate.formatDisplay(): String =
    "${this.day} ${this.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${this.year}"

fun Double.toCfa(): String = "${this.toLong()} FCFA"

fun Double.toDepositAmount(percent: Double = 0.3): Double = this * percent

fun Int.toNightsLabel(): String = if (this == 1) "1 nuit" else "$this nuits"

fun String.isValidEmail(): Boolean =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(this)

fun String.isValidPhone(): Boolean =
    Regex("^(6|2)[0-9]{8}$").matches(this)

fun String.isValidPassword(): Boolean = this.length >= 6