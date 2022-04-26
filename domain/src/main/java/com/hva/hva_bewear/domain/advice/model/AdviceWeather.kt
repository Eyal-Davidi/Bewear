package com.hva.hva_bewear.domain.advice.model

import com.hva.hva_bewear.domain.weather.model.WeatherDetails

data class AdviceWeather(
    val feelsLike : Double,
    val uvIndex : Double,
    val windSpeed : Double,
    val percentageOfPrecipitation : Double,
    val rain: Double,
)