package com.hva.bewear.domain.weather

import com.hva.bewear.domain.location.model.LocationData
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather

class GetWeather(private val repository: WeatherRepository) {

    suspend operator fun invoke(
        location: LocationData,
    ): Weather {
        return repository.getWeather(location)
    }
}