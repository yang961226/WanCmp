package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sundayting.wancmp.function.article.ArticleService
import com.sundayting.wancmp.utils.SPUtil
import com.sundayting.wancmp.utils.net.bean.isSuccess
import kotlinx.coroutines.launch
import org.koin.dsl.module


class HomeTabScreenModel(
    articleService: ArticleService
) : ScreenModel {

    private var _clickTime by mutableIntStateOf(0)
    val clickTime
        get() = _clickTime

    private val clickTimeKey = intPreferencesKey("click_time")

    init {
        screenModelScope.launch {
            val result = articleService.fetchBanner()
            if(result.isSuccess()){
                println("网络请求结果：${result.body}")
            }
        }
        screenModelScope.launch {
            _clickTime = SPUtil.userSpecific.getInt(clickTimeKey)
        }
    }

    fun updateClickTime() {
        screenModelScope.launch {
            SPUtil.userSpecific.saveInt(clickTimeKey, ++_clickTime)
        }
    }

}

val homeTabModule = module {
    factory { HomeTabScreenModel(get()) }
}