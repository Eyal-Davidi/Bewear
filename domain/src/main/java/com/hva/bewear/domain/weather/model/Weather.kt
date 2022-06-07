package com.hva.bewear.domain.weather.model

import java.time.Instant

data class Weather(
    val lastUsed: Instant,
    val cityName:String,
    val isCurrent: Boolean,
    val timeZoneOffset: Int,

    val daily: List<DailyWeather>,
    val hourly: List<HourlyWeather>,
)
