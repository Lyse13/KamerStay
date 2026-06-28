package com.kamerstay.app.data.store

object SessionStore {

    data class SessionData(
        val token: String,
        val fullName: String,
        val email: String,
        val phone: String,
        val role: String,
        val expiresAt: Long = 0L
    )

    private var saver: ((String, String, String, String, String, Long) -> Unit)? = null
    private var loader: (() -> SessionData?)? = null
    private var clearer: (() -> Unit)? = null

    fun init(
        saver: (token: String, fullName: String, email: String, phone: String, role: String, expiresAt: Long) -> Unit,
        loader: () -> SessionData?,
        clearer: () -> Unit
    ) {
        this.saver = saver
        this.loader = loader
        this.clearer = clearer
    }

    fun save(token: String, fullName: String, email: String, phone: String, role: String, expiresAt: Long) {
        saver?.invoke(token, fullName, email, phone, role, expiresAt)
    }

    fun load(): SessionData? = loader?.invoke()

    fun clear() = clearer?.invoke()
}