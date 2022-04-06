package com.hva.hva_bewear

import android.app.Application
import com.hva.hva_bewear.di.appModule
import com.hva.hva_bewear.di.dataModule
import com.hva.hva_bewear.di.domainModule
import com.hva.hva_bewear.di.presentationModule
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
