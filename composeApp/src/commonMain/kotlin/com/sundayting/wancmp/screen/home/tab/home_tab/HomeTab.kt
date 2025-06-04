package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.ic_home
import wancmp.composeapp.generated.resources.tab_home_title

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_home)
            val title = stringResource(Res.string.tab_home_title)
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
        val screenModel = koinScreenModel<HomeTabScreenModel>()
        Box(
            Modifier.Companion.fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Column {
                Text("当前点击数：${screenModel.clickTime}")
                Button(onClick = {
                    screenModel.updateClickTime()
                }) {
                    Text("点我加1")
                }
            }

        }
    }
}