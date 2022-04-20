package com.hva.hva_bewear.presentation.main.model

data class WeatherUIModel(
    val temperatureDisplay: String = "_",
    val feelsLikeTemperatureDisplay: String = "_",
    val windDisplay: String = "_",
    val iconUrl: String = "_",
    val windDegrees: Int = 0,
)
