package com.kamerstay.app.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppThemeController {
    var darkModeEnabled by mutableStateOf(false)
}