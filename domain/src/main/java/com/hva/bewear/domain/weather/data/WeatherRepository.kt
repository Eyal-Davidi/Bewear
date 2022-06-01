package com.hva.bewear.domain.weather.data

import com.hva.bewear.domain.location.model.LocationData
import com.hva.bewear.domain.weather.model.Weather

interface WeatherRepository {
    suspend fun getWeather(location: LocationData): Weather
}