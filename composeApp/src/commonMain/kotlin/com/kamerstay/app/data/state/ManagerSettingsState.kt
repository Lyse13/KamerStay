package com.kamerstay.app.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kamerstay.app.data.model.CurrencyOption
import com.kamerstay.app.data.model.LanguageOption

class ManagerSettingsState {
    var notificationsEnabled by mutableStateOf(true)
    var bookingAlertsEnabled by mutableStateOf(true)
    var darkModeEnabled by mutableStateOf(false)
    var selectedLanguage by mutableStateOf(LanguageOption("en", "English"))
    var selectedCurrency by mutableStateOf(CurrencyOption("XAF", "XAF (FCFA)"))
    var isLoading by mutableStateOf(false)

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