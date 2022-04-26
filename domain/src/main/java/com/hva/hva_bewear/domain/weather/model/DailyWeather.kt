package com.hva.hva_bewear.domain.weather.model

import java.time.LocalDateTime

data class DailyWeather(
    val date : LocalDateTime,
    val sunrise : Int,
    val sunset : Int,
    val moonrise : Int,
    val moonset : Int,
    val moonPhase : Double,
    val temperature : TemperatureDay,
    val feelsLike : FeelsLike,
    val pressure : Int,
    val humidity : Int,
    val dewPoint : Double,
    val windSpeed : Double,
    val windDegree: Int,
    val windGust : Double,
    val weather : List<WeatherDetails>,
    val clouds : Int,
    val percentageOfPrecipitation: Double,
    val uvIndex : Double,
    val rain: Double = 0.0,
    val snow: Double = 0.0,
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