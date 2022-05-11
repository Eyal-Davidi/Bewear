package com.hva.hva_bewear.data.weather

import android.content.Context
import com.hva.hva_bewear.data.weather.network.mapper.WeatherMapper.toDomain
import com.hva.hva_bewear.data.weather.network.WeatherService
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.Weather

class RemoteWeatherRepository(private val service: WeatherService, private val context: Context) : WeatherRepository {

    override suspend fun getWeather(location: String): Weather {
        return service.getWeather(context, location).toDomain()
    }
}