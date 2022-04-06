package com.hva.hva_bewear.domain.weather.model

data class HourlyWeather(
    val date : Int,
    val temperature : Double,
    val feelsLike : Double,
    val pressure : Int,
    val humidity : Int,
    val dewPoint : Double,
    val uvIndex : Double,
    val clouds : Int,
    val visibility : Int,
    val windSpeed : Double,
    val windDegree : Int,
    val windGust : Double,
    val weather : List<WeatherDetails>,
    val percentageOfPrecipitation : Double,
    val rain: HourlyPrecipitation = HourlyPrecipitation(),
    val snow: HourlyPrecipitation = HourlyPrecipitation(),
) {
    data class HourlyPrecipitation(
        val hour:Double = 0.0
    )
}