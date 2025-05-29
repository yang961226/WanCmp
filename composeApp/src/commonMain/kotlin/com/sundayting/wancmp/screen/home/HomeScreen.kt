package com.sundayting.wancmp.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.sundayting.wancmp.screen.home.tab.HomeTab
import com.sundayting.wancmp.screen.home.tab.MineTab

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(it.current.options.title)
                        },
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
                            onClick = { tabNavigator.current = MineTab },
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
            ) {
                CurrentTab()
            }
        }
    }

}