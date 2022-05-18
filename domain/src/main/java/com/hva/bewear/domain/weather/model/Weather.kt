package com.hva.bewear.domain.weather.model


data class Weather(
    val daily: List<DailyWeather>,
    val hourly: List<HourlyWeather>,
)
