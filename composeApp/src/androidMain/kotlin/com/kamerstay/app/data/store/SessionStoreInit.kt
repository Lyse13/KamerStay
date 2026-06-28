package com.kamerstay.app.data.store

import android.content.Context

fun initSessionStore(context: Context) {
    val prefs = context.getSharedPreferences("kamerstay_session", Context.MODE_PRIVATE)
    SessionStore.init(
        saver = { token, fullName, email, phone, role, expiresAt ->
            prefs.edit()
                .putString("token", token)
                .putString("fullName", fullName)
                .putString("email", email)
                .putString("phone", phone)
                .putString("role", role)
                .putLong("expiresAt", expiresAt)
                .apply()
        },
        loader = {
            val token = prefs.getString("token", null)?.takeIf { it.isNotBlank() }
                ?: return@init null
            SessionStore.SessionData(
                token     = token,
                fullName  = prefs.getString("fullName", "") ?: "",
                email     = prefs.getString("email", "") ?: "",
                phone     = prefs.getString("phone", "") ?: "",
                role      = prefs.getString("role", "TRAVELER") ?: "TRAVELER",
                expiresAt = prefs.getLong("expiresAt", 0L)
            )
        },
        clearer = {
            prefs.edit().clear().apply()
        }
    )
}