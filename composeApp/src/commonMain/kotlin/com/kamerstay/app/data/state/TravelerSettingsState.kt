package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.CurrencyOption
import com.kamerstay.app.core.theme.AppThemeController
import com.kamerstay.app.data.model.LanguageOption

class TravelerSettingsState {
    var selectedLanguage by mutableStateOf(LanguageOption("en", "English"))
    var selectedCurrency by mutableStateOf(CurrencyOption("XAF", "XAF (FCFA)"))
    var notificationsEnabled by mutableStateOf(true)
    var darkModeEnabled: Boolean
        get() = AppThemeController.darkModeEnabled
        set(value) { AppThemeController.darkModeEnabled = value }
    var showLanguagePicker by mutableStateOf(false)
    var showCurrencyPicker by mutableStateOf(false)

    val availableLanguages = listOf(
        LanguageOption("en", "English"),
        LanguageOption("fr", "Français")
    )

    val availableCurrencies = listOf(
        CurrencyOption("XAF", "XAF (FCFA)"),
        CurrencyOption("USD", "USD (\$)"),
        CurrencyOption("EUR", "EUR (€)")
    )
}