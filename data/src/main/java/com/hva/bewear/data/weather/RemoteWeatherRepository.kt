package com.hva.bewear.data.weather

import com.hva.bewear.data.generic.isBeforeCurrentHour
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.domain.location.model.LocationData
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toDomain
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toEntity
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val dataStore: WeatherDataStore,
) : WeatherRepository {

    override suspend fun getWeather(location: LocationData): Weather {
        return dataStore.getCachedWeather(location.cityName)?.takeIf {
            !it.created.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.toDomain() ?: service.getWeather(location).also {
            dataStore.cacheData(it.toEntity(location))
        }.toDomain(location)
    }
}