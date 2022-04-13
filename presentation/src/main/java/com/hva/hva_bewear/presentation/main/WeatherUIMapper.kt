package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.domain.weather.model.WeatherDetails
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import kotlin.math.round
import kotlin.math.roundToInt

object WeatherUIMapper {

    fun Weather.uiModel(day: Int = 0): WeatherUIModel = daily[day].uiModel()

    private fun DailyWeather.uiModel(): WeatherUIModel {
        return WeatherUIModel(
            temperatureDisplay = parseTemperature(temperature.day),
            feelsLikeTemperatureDisplay = "Feels like: \n${parseTemperature(feelsLike.day)}",
            windDisplay = "${setWindDirection(windDegree)} ${windSpeed.roundToInt()}",
            iconUrl = "https://openweathermap.org/img/wn/${weather[0].icon}@4x.png",
            windDegrees = windDegree,
        )
    }

    private fun parseTemperature(temperature: Double) = "${temperature.roundToInt()} °C"
    private fun setWindDirection(windDegree: Int): String {
        return when (windDegree) {
            in 34..78 -> "NE"
            in 79..123 -> "E"
            in 124..168 -> "SE"
            in 169..213 -> "S"
            in 214..258 -> "SW"
            in 259..303 -> "W"
            in 304..348 -> "NW"

            else -> "N"
        }
    }
}