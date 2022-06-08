package com.hva.bewear.di

import com.hva.bewear.data.avatar_type.RemoteAvatarTypeRepository
import com.hva.bewear.data.unit.RemoteUnitRepository
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.location.RemoteLocationRepository
import com.hva.bewear.data.weather.RemoteWeatherRepository
import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.bewear.domain.unit.data.UnitRepository
import com.hva.bewear.domain.location.LocationRepository
import com.hva.bewear.domain.weather.data.WeatherRepository
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single<AvatarTypeRepository> { RemoteAvatarTypeRepository(get()) }
    single<WeatherRepository> { RemoteWeatherRepository(get(), get(), get()) }
    single<UnitRepository> { RemoteUnitRepository(get()) }
    single<LocationRepository>{ RemoteLocationRepository(get(), get()) }

    // Data Stores
    single { WeatherDataStore(get()) }

    // Services



    single { WeatherService() }
    single {LocationService(get())}
}
