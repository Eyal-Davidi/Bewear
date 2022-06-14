package com.hva.bewear.presentation.main.model

import androidx.annotation.DrawableRes
import com.hva.bewear.domain.weather.model.HourlyWeather

data class WeatherUIModel(
    val temperatureDisplay: String = "_",
    val minMaxDisplay: String = "_",
    val feelsLikeTemperatureDisplay: String = "_",
    val hourlyWeather: List<HourlyWeather> = generateDefaultHourly(),
    val hourlyIcons: List<Int> = emptyList(),
    val windDisplay: String = "_",
    @DrawableRes val iconId: Int,
    @DrawableRes val backgroundId: Int,
    val useBlackText: Boolean = false,
    val windDegrees: Int = 0,
){
    companion object{
        private const val AMOUNT_OF_HOURS_IN_HOURLY = 24

        fun generateDefaultHourly() : List<HourlyWeather>{
            val list = arrayListOf<HourlyWeather>()
            for (i in 1..AMOUNT_OF_HOURS_IN_HOURLY) list.add(HourlyWeather())

            return list
        }
    }
}
