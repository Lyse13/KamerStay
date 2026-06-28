package com.kamerstay.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kamerstay.app.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KamerStay"
        ) {
            App()
        }
    }
}