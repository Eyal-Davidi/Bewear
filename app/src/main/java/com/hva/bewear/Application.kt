package com.hva.bewear

import android.app.Application
import com.hva.bewear.di.appModule
import com.hva.bewear.di.dataModule
import com.hva.bewear.di.domainModule
import com.hva.bewear.di.presentationModule
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
