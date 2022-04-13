package com.hva.hva_bewear.presentation.main.model

data class WeatherUIModel(
    val temperatureDisplay: String,
    val feelsLikeTemperatureDisplay: String,
    val windDisplay: String,
    val iconUrl: String,
    val windDegrees: Int,
)
