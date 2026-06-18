package com.kamerstay.app.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = BackgroundDark,
    surface = DarkNavy,
    surfaceVariant = SurfaceVariant,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = ErrorColor,
    onError = OnError,
    errorContainer = ErrorContainer,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = BackgroundLight,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = TextDark,
    onSurfaceVariant = OnSurfaceSecondary,
    outline = Divider,
    error = ErrorColor,
    onError = OnError,
)

@Composable
fun KamerStayTheme(
    darkMode: Boolean = AppThemeController.darkModeEnabled,
    content: @Composable () -> Unit
) {
    val appColors = if (darkMode) darkAppColors else lightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = if (darkMode) DarkColorScheme else LightColorScheme,
            typography = kamerStayTypography(),
            content = content
        )
    }
}