package com.sundayting.wancmp.function.article

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ArticleUiBean(
    val url: String,
    val id: String,
    val title: String,
    val date: String,
    val author: String,
    isLike: Boolean,
    val chapter: ArticleChapterBean,
    val superChapter: ArticleChapterBean
) {

    var isLike by mutableStateOf(isLike)

}

data class ArticleChapterBean(
    val id: String,
    val name: String
)