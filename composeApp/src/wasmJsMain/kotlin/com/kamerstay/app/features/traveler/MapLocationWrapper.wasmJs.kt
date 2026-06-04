package com.kamerstay.app.features.traveler

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kamerstay.app.core.theme.*

@Composable
actual fun MapLocationWrapper(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Map not available on Web",
            fontSize = 16.sp,
            color = OnSurfaceSecondary
        )
    }
}