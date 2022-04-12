package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import kotlin.math.round

object WeatherUIMapper {

    fun Weather.uiModel(day:Int = 0):WeatherUIModel = daily[day].uiModel()

    private fun DailyWeather.uiModel():WeatherUIModel {
        return WeatherUIModel(
            temperatureDisplay = parseTemperature(temperature.day),
            feelsLikeTemperatureDisplay = "Feels like: \n${parseTemperature(feelsLike.day)}",
        )
    }

    private fun parseTemperature(temperature:Double) = "${round(temperature).toInt()} °C"
}