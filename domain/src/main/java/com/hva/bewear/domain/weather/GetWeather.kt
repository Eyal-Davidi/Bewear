package com.hva.bewear.domain.weather

import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather

class GetWeather(private val repository: WeatherRepository) {

    suspend operator fun invoke(
        location: Location,
    ): Weather {
        return repository.getWeather(location)
    }
}