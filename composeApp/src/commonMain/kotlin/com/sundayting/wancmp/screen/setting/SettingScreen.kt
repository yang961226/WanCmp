package com.sundayting.wancmp.screen.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SettingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Box(
            Modifier.safeContentPadding().fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("我是Setting", color = Color.Black,modifier=Modifier.align(Alignment.TopCenter))
            Button(onClick = {
                navigator.pop()
            }) {
                Text("点我返回上一页")
            }
        }
    }
}