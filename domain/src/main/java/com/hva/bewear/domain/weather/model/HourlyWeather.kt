package com.hva.bewear.domain.weather.model

import java.time.LocalDateTime

data class HourlyWeather(
    val date : LocalDateTime = LocalDateTime.now(),
    val temperature : Double = 0.0,
    val feelsLike : Double = 0.0,
    val pressure : Int = 0,
    val humidity : Int = 0,
    val dewPoint : Double = 0.0,
    val uvIndex : Double = 0.0,
    val clouds : Int = 0,
    val visibility : Int = 0,
    val windSpeed : Double = 0.0,
    val windDegree : Int = 0,
    val windGust : Double = 0.0,
    val weather : List<WeatherDetails> = emptyList(),
    val percentageOfPrecipitation : Double = 0.0,
    val rain: HourlyPrecipitation = HourlyPrecipitation(),
    val snow: HourlyPrecipitation = HourlyPrecipitation(),
) {
    data class HourlyPrecipitation(
        val hour:Double = 0.0
    )
}