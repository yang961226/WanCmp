package com.sundayting.wancmp.utils.net.cookieJar

import androidx.datastore.preferences.core.stringPreferencesKey
import com.sundayting.wancmp.utils.SPUtil
import com.sundayting.wancmp.utils.clock
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DataStoreCookieJar : CookiesStorage {

    private val mutex = Mutex()

    private val keyFactory: (Url) -> String = {
        "cookie_key_${it.host}"
    }

    /**
     * 移除掉过期的cookie。
     * 注意：这个实现假设 maxAge 是 Cookie 的固有属性，并且在判断时与当前时间比较。
     * 更标准的做法是在 Cookie 被接收时，用 maxAge 计算一个绝对的 expires 时间。
     * 但既然 Ktor 的 Cookie 对象分别存储 maxAge 和 expires，我们这里都检查。
     */
    private fun List<Cookie>.filterValidCookies(): List<Cookie> {
        val currentTimeMillis = clock()
        return this.filter { cookie ->
            // 1. 检查 expires 属性 (绝对时间点)
            val expiresMillis = cookie.expires?.timestamp
            if (expiresMillis != null && expiresMillis <= currentTimeMillis) {
                return@filter false // 已过 expires 时间
            }
            // 2. 检查 maxAge 属性
            if (cookie.maxAge != null && cookie.maxAge!! <= 0) {
                return@filter false // maxAge 表示立即过期
            }

            true // 如果 expires 有效 (或没有) 并且 maxAge 不是 <=0 (或没有)
        }
    }


    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        val urlKey = stringPreferencesKey(keyFactory(requestUrl))
        val cookieListString = SPUtil.getString(urlKey)
        if (cookieListString.isNullOrEmpty()) {
            return emptyList()
        }
        val cookieList: List<Cookie> = Json.decodeFromString(cookieListString)
        return cookieList.filterValidCookies() // 使用新的过滤函数
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        if (cookie.name.isBlank()) {
            return
        }
        mutex.withLock {
            val urlKey = stringPreferencesKey(keyFactory(requestUrl))
            // 在添加新 Cookie 前，先获取并过滤现有的有效 Cookie
            val existingCookiesString = SPUtil.getString(urlKey)
            val validOldCookies = if (existingCookiesString.isNullOrEmpty()) {
                emptyList()
            } else {
                try {
                    Json.decodeFromString<List<Cookie>>(existingCookiesString).filterValidCookies()
                } catch (_: Exception) {
                    // 处理可能的反序列化错误
                    emptyList()
                }
            }.filterNot { it.name == cookie.name } // 移除同名旧 Cookie

            SPUtil.saveString(
                urlKey,
                Json.encodeToString(validOldCookies + cookie)
            )
        }
    }

    override fun close() {
    }
}