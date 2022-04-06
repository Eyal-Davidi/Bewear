package com.hva.hva_bewear.data.weather

import com.hva.hva_bewear.data.weather.network.WeatherMapper.toDomain
import com.hva.hva_bewear.data.weather.network.WeatherService
import com.hva.hva_bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.HourlyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.domain.weather.model.WeatherDetails

class RemoteWeatherRepository(private val service: WeatherService) : WeatherRepository {

    override suspend fun getWeather(): Weather {
        return service.getWeather().toDomain()
    }
}