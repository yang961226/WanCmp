package com.sundayting.wancmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sundayting.wancmp.utils.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "WanCmp",
        ) {
            App()
        }
    }
}