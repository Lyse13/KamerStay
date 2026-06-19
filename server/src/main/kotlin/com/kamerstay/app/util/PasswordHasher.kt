package com.kamerstay.app.util

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {
    fun hash(plainPassword: String): String {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray())
    }

    fun verify(plainPassword: String, hashedPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword)
        return result.verified
    }
}