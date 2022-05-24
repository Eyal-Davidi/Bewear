package com.hva.bewear.di

import com.hva.bewear.data.avatar_type.RemoteAvatarTypeRepository
import com.hva.bewear.data.unit.RemoteUnitRepository
import com.hva.bewear.data.weather.RemoteWeatherRepository
import com.hva.bewear.data.weather.network.ListOfLocations
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.bewear.domain.location.LocationPicker
import com.hva.bewear.domain.unit.data.UnitRepository
import com.hva.bewear.domain.weather.data.WeatherRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<AvatarTypeRepository> { RemoteAvatarTypeRepository(get()) }
    single<WeatherRepository> { RemoteWeatherRepository(get(), get()) }
    single<UnitRepository> { RemoteUnitRepository(get()) }

    // Services
    single { WeatherService() }

    factory<LocationPicker> { ListOfLocations() }
}
