package com.sundayting.wancmp.screen.home.tab.home_tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel


class HomeTabViewModel : ScreenModel {

    var clickTime by mutableIntStateOf(0)

}