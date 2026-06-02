package com.kamerstay.app

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.kamerstay.app.core.navigation.KamerStayNavGraph
import com.kamerstay.app.core.theme.KamerStayTheme
import com.kamerstay.app.di.initKoin
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    // Initialise Koin une seule fois
    LaunchedEffect(Unit) {
        initKoin()
    }

    KamerStayTheme {
        val navController = rememberNavController()
        KamerStayNavGraph(navController = navController)
    }
}