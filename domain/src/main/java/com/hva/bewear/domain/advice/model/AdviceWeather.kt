package com.hva.bewear.domain.advice.model

data class AdviceWeather(
    val feelsLike : Double,
    val uvIndex : Double,
    val windSpeed : Double,
    val percentageOfPrecipitation : Double,
    val rain: Double,
)