package com.kamerstay.app.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val background: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val inputText: Color,
)

val lightAppColors = AppColors(
    background = BackgroundLight,
    surface = Color.White,
    textPrimary = TextDark,
    textSecondary = OnSurfaceSecondary,
    inputBackground = Color.White,
    inputBorder = Color(0xFFDDE4EA),
    inputText = TextDark,
)

val darkAppColors = AppColors(
    background = DarkNavy,
    surface = SurfaceVariant,
    textPrimary = Color.White,
    textSecondary = OnSurfaceSecondary,
    inputBackground = SurfaceVariant,
    inputBorder = Outline,
    inputText = Color.White,
)

val LocalAppColors = staticCompositionLocalOf { lightAppColors }