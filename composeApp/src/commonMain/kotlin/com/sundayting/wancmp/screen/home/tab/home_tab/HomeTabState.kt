package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.runtime.mutableStateListOf
import com.sundayting.wancmp.function.article.ArticleService
import com.sundayting.wancmp.function.article.ArticleUiBean
import com.sundayting.wancmp.widgets.LoadStateHost

class HomeTabState : LoadStateHost() {

    var articlePageIndex = 0

    val bannerList = mutableStateListOf<ArticleService.BannerBean>()

    val articleList = mutableStateListOf<ArticleUiBean>()


}