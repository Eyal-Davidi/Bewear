package com.hva.bewear.data.weather

import com.hva.bewear.data.generic.isBeforeCurrentHour
import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.refreshLastUsedAndIsCurrent
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toDomain
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toEntity
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherResponseMapper.toDomain
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val dataStore: WeatherDataStore,
) : WeatherRepository {

    override suspend fun getWeather(location: Location): Weather {
        return dataStore.getCachedWeather(location.cityName)?.takeIf {
            !it.lastUsed.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.also {
            dataStore.cacheData(it.refreshLastUsedAndIsCurrent(location))
        }?.toDomain() ?: service.getWeather(location).also {
            dataStore.cacheData(it.toEntity(location))
        }.toDomain(location)
    }
}