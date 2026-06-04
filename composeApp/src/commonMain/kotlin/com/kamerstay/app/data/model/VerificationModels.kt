package com.kamerstay.app.data.model

data class VerificationDocument(
    val id: String,
    val type: DocumentType,
    val isUploaded: Boolean = false,
    val fileName: String = ""
)

enum class DocumentType {
    NATIONAL_ID_FRONT, NATIONAL_ID_BACK, BUSINESS_LICENSE
}

data class AmenityItem(
    val id: String,
    val icon: String,
    val name: String,
    val description: String,
    val category: AmenityCategory,
    val isEnabled: Boolean = false
)

enum class AmenityCategory {
    ESSENTIAL, LUXURY_WELLNESS, DINING, BUSINESS
}