package com.hva.bewear.domain.weather.data

import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.domain.weather.model.Weather

interface WeatherRepository {
    suspend fun getWeather(location: Location): Weather
}