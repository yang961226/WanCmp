package com.sundayting.wancmp.utils.net.cookieJar

import androidx.datastore.preferences.core.stringPreferencesKey
import com.sundayting.wancmp.utils.SPUtil
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DataStoreCookieJar : CookiesStorage {

    private val keyFactory: (Url) -> String = {
        "cookie_key_${it.host}"
    }

    private fun Cookie.maxAgeOrExpires(createdAt: Long): Long? =
        maxAge?.let { createdAt + it * 1000L } ?: expires?.timestamp

    /**
     * 移除掉过期的cookie
     */
    private fun List<Cookie>.filterMaxAgeOrExpires(): List<Cookie> {
        val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
        return this.filter { cookie ->
            val expiresMillis = cookie.expires?.timestamp
            expiresMillis == null || expiresMillis > currentTimeMillis
        }
    }


    override suspend fun get(requestUrl: Url): List<Cookie> {
        val urlKey = stringPreferencesKey(keyFactory(requestUrl))
        val cookieListString = SPUtil.getString(urlKey)
        if (cookieListString.isNullOrEmpty()) {
            return emptyList()
        }
        val cookieList: List<Cookie> = Json.decodeFromString(cookieListString)
        return cookieList.filterMaxAgeOrExpires()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val urlKey = stringPreferencesKey(keyFactory(requestUrl))
        val oldCookieList = SPUtil.getString(urlKey)?.let {
            Json.decodeFromString<List<Cookie>>(it)
        }.orEmpty().filterNot { it.name == cookie.name }.filterMaxAgeOrExpires()
        SPUtil.saveString(
            urlKey,
            Json.encodeToString(oldCookieList + cookie)
        )
    }

    override fun close() {
    }
}