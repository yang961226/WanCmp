package com.sundayting.wancmp

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.sundayting.wancmp.utils.initKoin
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "玩ComposeMultiplatform",
        ) {
            var downProgress by remember { mutableFloatStateOf(0f) }
            var initialized by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    KCEF.init(builder = {
                        installDir(File("kcef-bundle"))
                        settings {
                            cachePath = File("cache").absolutePath
                        }
                        progress {
                            onDownloading {
                                println("浏览器进度:${it}")
                                downProgress = it
                            }
                            onInitialized {
                                println("浏览器初始化完成")
                                initialized = true
                            }
                        }
                    })
                }
            }
            DisposableEffect(Unit) {
                onDispose { KCEF.disposeBlocking() }
            }
            App()
        }
    }
}