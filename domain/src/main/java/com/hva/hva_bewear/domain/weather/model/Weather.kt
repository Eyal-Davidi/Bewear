package com.hva.hva_bewear.domain.weather.model


data class Weather(
    val daily: List<DailyWeather> = emptyList(),
    val hourly: List<HourlyWeather> = emptyList(),
)
