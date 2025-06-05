package com.sundayting.wancmp.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.sundayting.wancmp.screen.home.tab.home_tab.HomeTab
import com.sundayting.wancmp.screen.home.tab.mine_tab.MineTab
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.login_title
import wancmp.composeapp.generated.resources.register_title

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        var showLoginSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()

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

                        val pagerState = rememberPagerState { 2 }

                        val icons = remember {
                            listOf(
                                Icons.Filled.PersonAdd,
                                Icons.AutoMirrored.Filled.Login
                            )
                        }

                        val contentDescriptions = listOf(
                            stringResource(Res.string.register_title), // 仍然为可访问性提供描述
                            stringResource(Res.string.login_title)
                        )

                        SingleChoiceSegmentedButtonRow(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            space = 20.dp
                        ) {
                            val selectedIndex = pagerState.currentPage
                            icons.forEachIndexed { index, icon ->
                                SegmentedButton(
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = 2
                                    ),
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    selected = index == selectedIndex,
                                    label = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 20.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = contentDescriptions[index]
                                            )

                                            Spacer(Modifier.width(5.dp))

                                            Text(
                                                contentDescriptions[index]
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            userScrollEnabled = false
                        ) {
                            Box(
                                Modifier.fillMaxWidth().aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("当前页面：${it}")
                            }
                        }
                    }
                }
                Box(Modifier.padding(paddingValues)) {
                    CurrentTab()
                }
            }
        }
    }

}