package com.hva.hva_bewear.main.provider

import androidx.annotation.DrawableRes
import com.hva.hva_bewear.R
import com.hva.hva_bewear.presentation.main.provider.WeatherIconProvider

class AppWeatherIconProvider : WeatherIconProvider {
    //TODO: fix this so there are night icons as well as day icons
    @DrawableRes
    override fun getWeatherIcon(type: String): Int {
        return when (type) {
            "01d", "01n" -> R.drawable.ic_action_sunny
            "02d", "02n" -> R.drawable.ic_action_partly_cloudy
            "03d", "03n" -> R.drawable.ic_action_cloudy
            "04d", "04n" -> R.drawable.ic_action_broken_cloudy
            "09d", "09n" -> R.drawable.ic_action_cloudy_rainy
            "10d", "10n" -> R.drawable.ic_action_sunny_rainy
            "11d", "11n" -> R.drawable.ic_action_thunder_storm
            "13d", "13n" -> R.drawable.ic_action_snow
            "50d", "50n" -> R.drawable.ic_action_mist
            else -> R.drawable.default_placeholder
        }

    }

    //TODO: fix this so there are night backgrounds as well as day backgrounds
    @DrawableRes
    override fun getWeatherBackground(type: String): Int {
        return when (type) {
            "01d", "01n" -> R.raw.sunny_weather
            "02d", "02n" -> R.raw.sunny_weather
            "03d", "03n" -> R.raw.cloudy_weather
            "04d", "04n" -> R.raw.sunny_weather
            "09d", "09n" -> R.raw.rainy_weather
            "10d", "10n" -> R.raw.sunny_weather
            "11d", "11n" -> R.raw.rainy_weather
            "13d", "13n" -> R.raw.snow_weather
            "50d", "50n" -> R.raw.cloudy_weather
            else -> R.drawable.default_placeholder
        }

    }
}