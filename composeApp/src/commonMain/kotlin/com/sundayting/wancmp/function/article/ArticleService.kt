package com.sundayting.wancmp.function.article

import com.sundayting.wancmp.utils.net.bean.NResult
import com.sundayting.wancmp.utils.net.bean.WanNResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ArticleService {

    @GET("banner/json/")
    suspend fun fetchBanner(): NResult<BannerBeanResultBean>

    @Serializable
    data class BannerBean(
        val imagePath: String,
        val url: String,
        val id: Long,
        val title: String,
    )

    @Serializable
    data class BannerBeanResultBean(
        override val data: List<BannerBean>?,
        override val errorCode: Int,
        override val errorMsg: String,
    ) : WanNResult<List<BannerBean>>()

    @GET("article/list/{path}/json/")
    suspend fun fetchArticleList(@Path("path") page: Int): NResult<ArticleResultBean>

    @Serializable
    class ArticleResultBean(
        override val data: ArticleListBean?,
        override val errorCode: Int,
        override val errorMsg: String,
    ) : WanNResult<ArticleListBean>()

    @Serializable
    data class ArticleListBean(
        val curPage: Int,
        val pageCount: Int,
        @SerialName("datas")
        val list: List<ArticleBean>,
    )

    @Serializable
    data class ArticleBean(
        val id: Long,
        val title: String,
        val envelopePic: String,
        val desc: String,
        val niceDate: String,
        val fresh: Boolean,
        val shareUser: String,
        val author: String,
        val chapterId: Long,
        val chapterName: String,
        val superChapterId: Long,
        val superChapterName: String,
        val link: String,
        val collect: Boolean,
        val isStick: Boolean = false,
        val tags: List<ArticleTag>,
    )

    @Serializable
    data class ArticleTag(
        val name: String,
        val url: String,
    )


}