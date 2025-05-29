package com.sundayting.wancmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.sundayting.wancmp.screen.home.HomeScreen

@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) {
            SlideTransition(
                it,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}