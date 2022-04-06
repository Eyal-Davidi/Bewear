package com.hva.hva_bewear.domain.weather

import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.Weather

class GetWeather(private val repository: WeatherRepository) {

    suspend operator fun invoke(): Weather {
        return repository.getWeather()
    }
}