package com.kamerstay.app

import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.kamerstay.app.core.navigation.KamerStayNavGraph
import com.kamerstay.app.core.theme.KamerStayTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    KamerStayTheme {
        val navController = rememberNavController()
        KamerStayNavGraph(navController = navController)
    }
}