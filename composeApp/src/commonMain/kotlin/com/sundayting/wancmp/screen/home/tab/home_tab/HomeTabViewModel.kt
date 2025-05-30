package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.intPreferencesKey
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.sundayting.wancmp.utils.SPUtil
import kotlinx.coroutines.launch


class HomeTabViewModel : ScreenModel {

    private var _clickTime by mutableIntStateOf(0)
    val clickTime
        get() = _clickTime

    private val clickTimeKey = intPreferencesKey("click_time")

    init {
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