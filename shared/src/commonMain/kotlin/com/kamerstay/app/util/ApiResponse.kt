package com.kamerstay.app.util

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean = false,
    val message: String = "",
    val data: T? = null,
    val error: String? = null
)

@Serializable
data class AuthResponse(
    val token: String = "",
    val user: com.kamerstay.app.model.User = com.kamerstay.app.model.User()
)

@Serializable
data class PaginatedResponse<T>(
    val data: List<T> = emptyList(),
    val total: Int = 0,
    val page: Int = 1,
    val limit: Int = 10,
    val hasMore: Boolean = false
)
