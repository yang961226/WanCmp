package com.sundayting.wancmp.widgets

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * @see LoadStateHost
 */
@Composable
fun LoadStateHostContainer(
    modifier: Modifier = Modifier,
    state: LoadStateHost,
    needShowRefreshing: Boolean,
    content: @Composable BoxScope.() -> Unit
) {

    val isNeedShowRefreshing by rememberUpdatedState(needShowRefreshing)

    val loadState by remember {
        derivedStateOf {
            if (state.isRefreshing && isNeedShowRefreshing) {
                LoadState.Refreshing
            } else if (state.isEmpty) {
                LoadState.Empty
            } else if (state.isError) {
                LoadState.Error
            } else {
                LoadState.Normal
            }
        }
    }

    Crossfade(
        targetState = loadState,
        modifier = modifier
    ) {
        when (it) {
            LoadState.Refreshing -> Box(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Text("刷新中")
            }

            LoadState.Error -> Box(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Text("错误")
            }

            LoadState.Empty -> Box(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Text("内容为空")
            }

            LoadState.Normal -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }


    }


}

enum class LoadState {

    Refreshing,
    Error,
    Empty,
    Normal

}