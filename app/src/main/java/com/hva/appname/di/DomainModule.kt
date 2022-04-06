package com.hva.appname.di

import com.hva.appname.domain.text.GetText
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetText(get()) }
}
