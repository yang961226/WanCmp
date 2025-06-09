package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import com.sundayting.wancmp.screen.web.ArticleWebScreen
import com.sundayting.wancmp.widgets.LoadStateHostContainer
import com.sundayting.wancmp.widgets.LoadingMoreWidget
import com.sundayting.wancmp.widgets.toLoadingMoreState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wancmp.composeapp.generated.resources.Res
import wancmp.composeapp.generated.resources.ic_home
import wancmp.composeapp.generated.resources.ic_loading_pic
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
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val showFab by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 5
            }
        }
        val navigator = LocalNavigator.currentOrThrow.parent!!
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val preferredItemWidth = maxWidth / 2
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
                        Modifier.fillMaxSize(),
                        state = listState
                    ) {
                        if (state.bannerList.isNotEmpty()) {
                            item(key = "banner", contentType = "banner") {
                                Column {
                                    val carouselState =
                                        rememberCarouselState { state.bannerList.size }
                                    HorizontalMultiBrowseCarousel(
                                        state = carouselState,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        preferredItemWidth = preferredItemWidth,
                                        itemSpacing = 16.dp,
                                        contentPadding = PaddingValues(horizontal = 16.dp)
                                    ) { i ->
                                        val item = state.bannerList[i]
                                        AsyncImage(
                                            model = item.imagePath,
                                            contentDescription = "banner：${item.title}",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(2f / 1f)
                                                .maskClip(MaterialTheme.shapes.extraLarge)
                                                .maskBorder(
                                                    BorderStroke(
                                                        0.5.dp,
                                                        MaterialTheme.colorScheme.outline
                                                    ), MaterialTheme.shapes.extraLarge
                                                ),
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(Res.drawable.ic_loading_pic),
                                            fallback = painterResource(Res.drawable.ic_loading_pic),
                                        )
                                    }
                                    HorizontalDivider()
                                }

                            }
                        }
                        itemsIndexed(
                            items = state.articleList,
                            contentType = { _, _ -> 1 },
                            key = { _, article -> article.id }
                        ) { index, article ->
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
                                    navigator.push(ArticleWebScreen(url = article.url))
                                }
                            )
                            HorizontalDivider()
                        }
                        if (state.articleList.isNotEmpty()) {
                            item {
                                LaunchedEffect(state.articleList.size) {
                                    screenModel.loadMore()
                                    launch {
                                        listState.scrollToItem(listState.layoutInfo.totalItemsCount - 1)
                                    }
                                }
                                LoadingMoreWidget(
                                    Modifier.fillMaxWidth(),
                                    state = state.toLoadingMoreState()
                                )
                            }
                        }
                    }
                }
            }


            AnimatedVisibility(
                visible = showFab,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 30.dp, end = 20.dp),
                enter = fadeIn() + scaleIn(
                    initialScale = 0.1f,
                ),
                exit = fadeOut() + scaleOut(
                    targetScale = 0.1f,
                )
            ) {


                FloatingActionButtonDefaults.elevation()
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0) // 点击时，平滑滚动到列表顶部
                        }
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 6.dp,
                        hoveredElevation = 6.dp,
                        focusedElevation = 6.dp

                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp, // 使用向上的箭头图标
                        contentDescription = "回到顶部" // 为可访问性添加描述
                    )
                }
            }
        }
    }
}