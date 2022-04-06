package com.hva.appname.di

import com.hva.appname.data.text.RemoteTextRepository
import com.hva.appname.data.text.network.TextService
import com.hva.appname.domain.text.data.TextRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<TextRepository> { RemoteTextRepository(get()) }

    // Services
    single { TextService() }
}
