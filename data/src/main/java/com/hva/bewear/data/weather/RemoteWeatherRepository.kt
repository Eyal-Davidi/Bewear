package com.hva.bewear.data.weather

import com.hva.bewear.data.generic.isBeforeCurrentHour
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.refreshLastUsedAndIsCurrent
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toDomain
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toEntity
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherResponseMapper.toDomain
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val dataStore: WeatherDataStore,
    private val locationService: LocationService,
) : WeatherRepository {

    override suspend fun getWeather(location: Location): Weather {
        var loc = location
        if(location.lat == null|| location.lon == null){
             loc = locationService.returnLocation(location)
        }

        return dataStore.getCachedWeather(loc.cityName)?.takeIf {
            !it.hourly[0].date.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.also {
            dataStore.cacheData(it.refreshLastUsedAndIsCurrent(loc))
        }?.toDomain() ?: service.getWeather(loc).also {
            dataStore.cacheData(it.toEntity(loc))
        }.toDomain(loc)
    }
}