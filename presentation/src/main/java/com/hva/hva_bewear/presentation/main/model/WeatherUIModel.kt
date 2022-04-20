package com.hva.hva_bewear.presentation.main.model

import androidx.annotation.DrawableRes

data class WeatherUIModel(
    val temperatureDisplay: String = "_",
    val feelsLikeTemperatureDisplay: String = "_",
    val windDisplay: String = "_",
    val iconId: Int = 0,
    val windDegrees: Int = 0,
)
