package com.hva.bewear.data.weather

import android.content.Context
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toDomain
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.domain.location.Coordinates
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather

class RemoteWeatherRepository(private val service: WeatherService, private val context: Context) : WeatherRepository {

    override suspend fun getWeather(location: String, coordinates: Coordinates): Weather {
        return service.getWeather(context, location, coordinates).toDomain()

    }
}