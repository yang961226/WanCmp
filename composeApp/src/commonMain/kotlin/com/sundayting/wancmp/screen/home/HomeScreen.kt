package com.sundayting.wancmp.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.sundayting.wancmp.function.loginAndRegister.LoginAndRegister
import com.sundayting.wancmp.screen.home.tab.home_tab.HomeTab
import com.sundayting.wancmp.screen.home.tab.mine_tab.MineTab

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        var showLoginSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        TabNavigator(HomeTab) {
            Scaffold(
                topBar = {
                    Surface(
                        shadowElevation = 2.dp
                    ) {
                        TopAppBar(
                            title = {
                                Text(it.current.options.title)
                            },
                            expandedHeight = 46.dp,
                            actions = {
                                if (it.current == HomeTab) {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                    }

                },
                bottomBar = {
                    val tabNavigator = LocalTabNavigator.current
                    NavigationBar {
                        NavigationBarItem(
                            selected = tabNavigator.current == HomeTab,
                            onClick = { tabNavigator.current = HomeTab },
                            icon = {
                                Icon(
                                    painter = HomeTab.options.icon!!,
                                    contentDescription = HomeTab.options.title
                                )
                            },
                            label = {
                                Text(HomeTab.options.title)
                            }
                        )
                        NavigationBarItem(
                            selected = tabNavigator.current == MineTab,
                            onClick = { showLoginSheet = true },
                            icon = {
                                Icon(
                                    painter = MineTab.options.icon!!,
                                    contentDescription = MineTab.options.title
                                )
                            },
                            label = {
                                Text(MineTab.options.title)
                            }
                        )
                    }
                }
            ) { paddingValues ->
                if (showLoginSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showLoginSheet = false
                        },
                        sheetState = sheetState
                    ) {

                        LoginAndRegister.Content()

                    }
                }
                Box(Modifier.padding(paddingValues)) {
                    CurrentTab()
                }
            }
        }
    }

}