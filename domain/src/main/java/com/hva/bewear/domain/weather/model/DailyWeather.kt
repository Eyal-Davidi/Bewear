package com.hva.bewear.domain.weather.model

import java.time.LocalDateTime

data class DailyWeather(
    val date : LocalDateTime,
    val temperature : TemperatureDay,
    val feelsLike : FeelsLike,
    val windSpeed : Double,
    val windDegree: Int,
    val weather : List<WeatherDetails>,
    val percentageOfPrecipitation: Double,
    val uvIndex : Double,
    val rain: Double = 0.0,
) {
    data class TemperatureDay(
        val day: Double,
        val min: Double,
        val max: Double,
        val night: Double,
        val evening: Double,
        val morning: Double
    )

    data class FeelsLike(
        val day : Double,
        val night : Double,
        val evening : Double,
        val morning : Double
    )
}