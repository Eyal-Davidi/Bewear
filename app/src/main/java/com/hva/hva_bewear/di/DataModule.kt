package com.hva.hva_bewear.di

import com.hva.hva_bewear.data.text.RemoteTextRepository
import com.hva.hva_bewear.data.text.network.TextService
import com.hva.hva_bewear.data.weather.RemoteWeatherRepository
import com.hva.hva_bewear.data.weather.network.GetLocation
import com.hva.hva_bewear.data.weather.network.ListOfLocations
import com.hva.hva_bewear.data.weather.network.WeatherService
import com.hva.hva_bewear.domain.location.LocationPicker
import com.hva.hva_bewear.domain.text.data.TextRepository
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<TextRepository> { RemoteTextRepository(get()) }

    // Services
    single { TextService() }

    single<WeatherRepository> { RemoteWeatherRepository(get(), get()) }

    single { WeatherService(get()) }

    single { GetLocation(get()) }
    factory<LocationPicker> { ListOfLocations() }
}
