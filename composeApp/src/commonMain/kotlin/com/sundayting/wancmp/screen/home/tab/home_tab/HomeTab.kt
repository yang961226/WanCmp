package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.sundayting.wancmp.widgets.LoadStateHostContainer
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<HomeTabScreenModel>()
        val state = screenModel.state

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = screenModel::refresh
        ) {
            LoadStateHostContainer(
                modifier = Modifier.fillMaxSize(),
                state = state,
                needShowRefreshing = false,
            ) {
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    itemsIndexed(state.articleList) { index, article ->
                        ListItem(
                            overlineContent = { Text("${article.author}・${article.date}") },
                            headlineContent = {
                                Text(
                                    text = article.title,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            },
                            supportingContent = { Text("${article.superChapter.name}・${article.chapter.name}") },
                            trailingContent = {
                                IconButton(
                                    onClick = {

                                    },
                                ) {
                                    Icon(
                                        if (article.isLike) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (article.isLike) MaterialTheme.colorScheme.primary else LocalContentColor.current
                                    )
                                }
                            },
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = ripple()
                            ) {

                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}