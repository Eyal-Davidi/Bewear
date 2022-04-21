package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import com.hva.hva_bewear.presentation.main.provider.WeatherIconProvider
import kotlin.math.pow
import kotlin.math.roundToInt

object WeatherUIMapper {

    fun Weather.uiModel(day: Int = 0, idProvider: WeatherIconProvider): WeatherUIModel = daily[day].uiModel(idProvider)

    //fun ClothingAdvice.uiModel(idProvider: AvatarIdProvider, stringProvider: TextAdviceStringProvider): AdviceUIModel {
    private fun DailyWeather.uiModel(idProvider: WeatherIconProvider): WeatherUIModel {
        return WeatherUIModel(
            temperatureDisplay = parseTemperature(temperature.day),
            feelsLikeTemperatureDisplay = "Feels like: \n${parseTemperature(feelsLike.day)}",
            windDisplay = "${setWindDirection(windDegree)} ${calculateBeaufortScale(windSpeed)}",
            iconId = idProvider.getWeatherIcon(weather[0].icon),
            // Because we want the arrow to point towards the direction the wind is blowing we add 180° to it
            windDegrees = windDegree + 180,
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

    /**
     * Uses the general formula used to calculate the beaufort scale, v = 0.836B^(3/2) (v = m/s and B = beaufort scale)
     * This formula is converted to take the form of B = ...v^... so in this case B = (v/0.836)^(2/3)
     */
    private fun calculateBeaufortScale(windSpeed:Double):Int {
        return ((windSpeed / 0.836).pow(2.0 / 3.0)).roundToInt()
    }
}