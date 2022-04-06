package com.hva.appname

import android.app.Application
import com.hva.appname.di.appModule
import com.hva.appname.di.dataModule
import com.hva.appname.di.domainModule
import com.hva.appname.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Application)
            modules(listOf(appModule, dataModule, presentationModule, domainModule))
        }
    }
}
