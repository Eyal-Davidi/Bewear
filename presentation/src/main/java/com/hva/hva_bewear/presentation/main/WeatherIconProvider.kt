package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.weather.model.Weather

interface WeatherIconProvider {
    fun getWeatherIcon(type: String): Int
}