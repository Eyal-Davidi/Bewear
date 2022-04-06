package com.hva.hva_bewear.data.weather.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(
    @SerialName("timezone")
    val timeZone: String,
    @SerialName("timezone_offset")
    val timeZoneOffset: Int,

    @SerialName("daily")
    val daily: List<DailyWeatherResponse>,
    @SerialName("hourly")
    val hourly: List<HourlyWeatherResponse>,
) {
    override fun toString(): String {
        return "$timeZone\n$timeZoneOffset\n\n$daily\n\n$hourly"
    }
}