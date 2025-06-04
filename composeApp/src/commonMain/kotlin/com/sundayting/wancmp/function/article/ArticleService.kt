package com.sundayting.wancmp.function.article

import com.sundayting.wancmp.utils.net.bean.NResult
import com.sundayting.wancmp.utils.net.bean.WanNResult
import de.jensklingenberg.ktorfit.http.GET
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

}