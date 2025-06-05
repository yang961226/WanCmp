package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.ui.util.fastDistinctBy
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sundayting.wancmp.function.article.ArticleChapterBean
import com.sundayting.wancmp.function.article.ArticleService
import com.sundayting.wancmp.function.article.ArticleUiBean
import com.sundayting.wancmp.utils.net.bean.isSuccess
import com.sundayting.wancmp.utils.net.bean.requireData
import com.sundayting.wancmp.widgets.checkBeforeLoad
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.dsl.module


class HomeTabScreenModel(
    private val articleService: ArticleService
) : ScreenModel {

    val state = HomeTabState()

    fun refresh() = load(true)
    fun loadMore() = load(false)

    init {
        refresh()
    }

    private var loadJob: Job? = null
    private fun load(isRefresh: Boolean) {
        if (state.checkBeforeLoad(isRefresh) { loadJob?.cancel() }) {
            loadJob = screenModelScope.launch {
                val fetchBannerJob = launch {
                    if (isRefresh) {
                        val result = articleService.fetchBanner()
                        if (result.isSuccess()) {
                            state.bannerList.addAll(result.body.data.orEmpty())
                        }
                    }
                }

                val fetchArticleListJob = launch {
                    val result =
                        articleService.fetchArticleList(if (isRefresh) 0 else state.articlePageIndex)
                    if (result.isSuccess()) {
                        val data = result.body.requireData()
                        if (isRefresh) {
                            state.articleList.clear()
                        }
                        val newList = state.articleList + data.list.map {
                            ArticleUiBean(
                                id = it.id.toString(),
                                title = it.title,
                                date = it.niceDate,
                                author = it.author.ifEmpty { it.shareUser },
                                isLike = it.collect,
                                chapter = ArticleChapterBean(
                                    id = it.chapterId.toString(),
                                    name = it.chapterName
                                ),
                                superChapter = ArticleChapterBean(
                                    id = it.superChapterId.toString(),
                                    name = it.superChapterName
                                )
                            )
                        }.fastDistinctBy { it.id }
                        state.articleList.addAll(newList)
                        state.articlePageIndex = data.curPage
                        state.changeCanLoadMore(data.pageCount != data.curPage)
                        state.changeEmpty(state.articleList.isEmpty())
                    } else if (state.articleList.isEmpty()) {
                        state.changeError(true)
                    }
                }
                fetchArticleListJob.join()
                fetchBannerJob.join()
            }.apply {
                invokeOnCompletion {
                    state.endLoad()
                }
            }
        }
    }

}

val homeTabModule = module {
    factory { HomeTabScreenModel(get()) }
}