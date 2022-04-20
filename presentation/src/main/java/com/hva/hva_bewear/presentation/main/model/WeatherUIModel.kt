package com.hva.hva_bewear.presentation.main.model

import androidx.annotation.DrawableRes

data class WeatherUIModel(
    val temperatureDisplay: String = "_",
    val feelsLikeTemperatureDisplay: String = "_",
    val windDisplay: String = "_",
    @DrawableRes val iconId: Int,
    val windDegrees: Int = 0,
)
