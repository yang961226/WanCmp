package com.sundayting.wancmp.screen.jigsaw


import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import cafe.adriel.voyager.core.model.ScreenModel
import com.sundayting.wancmp.screen.jigsaw.JigsawScreen.Companion.JigSawScreenState
import com.sundayting.wancmp.screen.jigsaw.JigsawScreen.Companion.JigsawBean


class JigsawScreenModel : ScreenModel {

    val state = JigSawScreenState(
        gameSize = IntSize(3, 3)
    ).apply {
        jigsawList.add(JigsawBean(index = 2, offset = IntOffset(0, 0)))
        jigsawList.add(JigsawBean(index = 4, offset = IntOffset(1, 0)))
        jigsawList.add(JigsawBean(index = 6, offset = IntOffset(2, 0)))
        jigsawList.add(JigsawBean(index = 5, offset = IntOffset(0, 1)))
        jigsawList.add(JigsawBean(index = 3, offset = IntOffset(1, 1)))
        jigsawList.add(JigsawBean(index = 8, offset = IntOffset(2, 1)))
        jigsawList.add(JigsawBean(index = 1, offset = IntOffset(0, 2)))
        jigsawList.add(JigsawBean(index = 7, offset = IntOffset(1, 2)))
    }
}