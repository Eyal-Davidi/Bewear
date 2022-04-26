package com.hva.hva_bewear.presentation.main.model

import androidx.annotation.DrawableRes
import com.hva.hva_bewear.domain.weather.model.HourlyWeather

data class WeatherUIModel(
    val temperatureDisplay: String = "_",
    val minMaxDisplay: String = "_",
    val feelsLikeTemperatureDisplay: String = "_",
    val hourlyWeather: List<HourlyWeather> = emptyList(),
    val windDisplay: String = "_",
    @DrawableRes val iconId: Int,
    @DrawableRes val backgroundId: Int,
    val windDegrees: Int = 0,
)
