package com.kamerstay.app.data.model

data class FaqItem(
    val id: String,
    val categoryId: String,
    val question: String,
    val answer: String
)

data class FaqCategory(
    val id: String,
    val label: String,
    val icon: String
)