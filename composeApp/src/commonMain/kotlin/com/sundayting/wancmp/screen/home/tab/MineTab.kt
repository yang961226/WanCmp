package com.sundayting.wancmp.screen.home.tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.ic_mine
import wancmp.composeapp.generated.resources.tab_mine_title

object MineTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_mine)
            val title = stringResource(Res.string.tab_mine_title)
            return remember {
                derivedStateOf {
                    TabOptions(
                        index = 1u,
                        title = title,
                        icon = icon
                    )
                }
            }.value
        }

    @Composable
    override fun Content() {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("我是MineTab")
        }
    }
}