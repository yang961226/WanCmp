package com.sundayting.wancmp.utils.net

import com.sundayting.wancmp.function.article.ArticleService
import com.sundayting.wancmp.function.article.createArticleService
import com.sundayting.wancmp.utils.net.converter.WanConverterFactory
import com.sundayting.wancmp.utils.net.cookieJar.DataStoreCookieJar
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ktorfitModule = module {
    single {
        val wanConverterFactory = get<WanConverterFactory>(named("WanConverterFactory"))
        ktorfit {
            baseUrl("https://www.wanandroid.com/")
            converterFactories(wanConverterFactory)
            httpClient(get<HttpClient>())
        }
    }
    single(named("WanConverterFactory")) {
        WanConverterFactory()
    }
    single<CookiesStorage> {
        DataStoreCookieJar()
    }
    single<HttpClient> {
        HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("网络请求日志：${message}")
                        level = LogLevel.ALL
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(HttpCookies) {
                storage = get()
            }
        }
    }
}

val networkModule = module {
    single<ArticleService> {
        get<Ktorfit>().createArticleService()
    }
}