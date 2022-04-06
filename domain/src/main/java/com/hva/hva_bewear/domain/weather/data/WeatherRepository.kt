package com.hva.hva_bewear.domain.weather.data

import com.hva.hva_bewear.domain.weather.model.Weather

interface WeatherRepository {
    suspend fun getWeather(): Weather
}