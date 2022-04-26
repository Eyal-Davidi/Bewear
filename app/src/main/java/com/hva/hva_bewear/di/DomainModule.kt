package com.hva.hva_bewear.di

import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.location.PassLocation
import com.hva.hva_bewear.domain.text.GetText
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.domain.weather.LocationPicker
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetText(get()) }
    factory { GetWeather(get()) }
    factory { GetClothingAdvice(get()) }
    factory { LocationPicker }
    factory { PassLocation(get()) }
}
