package com.sergiom.cabishop.app

import android.app.Application
import android.content.Context
import com.sergiom.cabishop.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CabiShopApp: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this
    }

    companion object {
        lateinit var instance: CabiShopApp

        fun getAppContext(): Context = instance.applicationContext
    }

}