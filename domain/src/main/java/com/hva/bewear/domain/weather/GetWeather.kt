package com.hva.bewear.domain.weather

import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather

class GetWeather(private val repository: WeatherRepository) {

    suspend operator fun invoke(location: String): Weather {
        return repository.getWeather(location)
    }
}