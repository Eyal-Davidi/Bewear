package com.hva.hva_bewear.main

import android.content.Context
import android.media.Image
import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.presentation.main.WeatherIconProvider

//class WeatherIconProvider :int {
class AppWeatherIconProvider : WeatherIconProvider {

    override fun getWeatherIcon(type: String): Int {
        return (
            when (type) {
                "01d" -> R.drawable.ic_action_sunny
                "02d" -> R.drawable.ic_action_partly_cloudy
                "03d" -> R.drawable.ic_action_cloudy
                "04d" -> R.drawable.ic_action_broken_cloudy
                "09d" -> R.drawable.ic_action_cloudy_rainy
                "10d" -> R.drawable.ic_action_sunny_rainy
                "11d" -> R.drawable.ic_action_thunder_storm
                "13d" -> R.drawable.ic_action_snow
                "50d" -> R.drawable.ic_action_mist

//                "03d" -> R.drawable.ic_action_cloudy
//                "03d" -> R.drawable.ic_action_cloudy
//                "03d" -> R.drawable.ic_action_cloudy
//                "03d" -> R.drawable.ic_action_cloudy
//                "03d" -> R.drawable.ic_action_cloudy

                   else -> R.drawable.ic_action_mist
            }
        )
    }
}