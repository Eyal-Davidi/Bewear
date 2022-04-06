package com.hva.hva_bewear.di

import com.hva.hva_bewear.domain.text.GetText
import com.hva.hva_bewear.domain.weather.GetWeather
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetText(get()) }
    factory { GetWeather(get()) }
}
