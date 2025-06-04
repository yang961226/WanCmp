package com.sundayting.wancmp

import android.app.Application
import com.sundayting.wancmp.utils.initKoin
import org.koin.android.ext.koin.androidContext

class WanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WanApplication)
        }
    }

}