package com.sundayting.wancmp.utils

import com.sundayting.wancmp.screen.home.tab.home_tab.homeTabModule
import com.sundayting.wancmp.utils.net.ktorfitModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        includes(config)
        modules(
            homeTabModule,
            ktorfitModule
        )
    }
}