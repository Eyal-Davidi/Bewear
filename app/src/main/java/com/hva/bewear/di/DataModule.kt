package com.hva.bewear.di

import com.hva.bewear.data.avatar_type.RemoteAvatarTypeRepository
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.location.RemoteLocationRepository
import com.hva.bewear.data.text.RemoteTextRepository
import com.hva.bewear.data.weather.RemoteWeatherRepository
import com.hva.bewear.data.weather.network.ListOfLocations
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.bewear.domain.location.LocationPicker
import com.hva.bewear.domain.location.LocationRepository
import com.hva.bewear.domain.text.data.TextRepository
import com.hva.bewear.domain.weather.data.WeatherRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<TextRepository> { RemoteTextRepository(get()) }
    single<AvatarTypeRepository> { RemoteAvatarTypeRepository(get()) }
    single<WeatherRepository> { RemoteWeatherRepository(get(), get()) }
    single<LocationRepository>{RemoteLocationRepository(get())}

    // Services



    single { WeatherService(get()) }
    single {LocationService()}
    factory<LocationPicker> { ListOfLocations() }
}
