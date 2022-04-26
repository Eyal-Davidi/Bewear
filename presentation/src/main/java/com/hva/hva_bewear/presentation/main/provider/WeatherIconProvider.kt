package com.hva.hva_bewear.presentation.main.provider

import com.hva.hva_bewear.domain.weather.model.Weather

interface WeatherIconProvider {
    fun getWeatherIcon(type: String): Int
    fun getWeatherBackground(type: String): Int
}