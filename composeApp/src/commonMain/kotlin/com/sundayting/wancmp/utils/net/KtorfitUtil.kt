package com.sundayting.wancmp.utils.net

import com.sundayting.wancmp.utils.net.converter.WanConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ktorfitModule = module {
    single {
        val wanConverterFactory = get<WanConverterFactory>(named("WanConverterFactory"))
        ktorfit {
            baseUrl("https://www.wanandroid.com/")
            converterFactories(wanConverterFactory)
        }
    }
    single(named("WanConverterFactory")) {
        WanConverterFactory()
    }
}