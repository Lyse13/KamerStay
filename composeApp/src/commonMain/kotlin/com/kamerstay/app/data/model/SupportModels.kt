package com.kamerstay.app.data.model

data class SupportCategory(
    val id: String,
    val icon: String,
    val title: String,
    val subtitle: String = "",
    val isFeatured: Boolean = false
)

data class SupportContact(
    val id: String,
    val icon: String,
    val title: String,
    val subtitle: String
)

data class TrendingTopic(
    val id: String,
    val title: String
)