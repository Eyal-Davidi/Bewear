package com.hva.bewear.data.weather.data.entity

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class WeatherEntity(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    val created: Instant,
    @SerialName("city_name")
    val cityName:String,
    @SerialName("is_current")
    val isCurrent: Boolean,

    @SerialName("timezone_offset")
    val timeZoneOffset: Int,
    @SerialName("lat")
    val lat: Double,
    @SerialName("lon")
    val lon: Double,

    @SerialName("daily")
    val daily: List<DailyWeatherEntity>,
    @SerialName("hourly")
    val hourly: List<HourlyWeatherEntity>,
)