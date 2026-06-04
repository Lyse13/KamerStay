package com.kamerstay.app.features.traveler

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
actual fun MapLocationWrapper(navController: NavController) {
    MapLocationScreen(navController)
}