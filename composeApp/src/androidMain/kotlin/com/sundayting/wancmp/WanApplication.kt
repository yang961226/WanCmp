package com.sundayting.wancmp

import android.app.Application

class WanApplication : Application() {

    companion object {
        lateinit var instance: WanApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}