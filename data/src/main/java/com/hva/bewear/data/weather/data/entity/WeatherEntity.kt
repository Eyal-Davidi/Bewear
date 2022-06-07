package com.hva.bewear.data.weather.data.entity

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant


@Serializable
data class WeatherEntity(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    var lastUsed: Instant,
    @SerialName("city_name")
    val cityName:String,
    @SerialName("state")
    val state:String?,
    @SerialName("country")
    val country:String,
    @SerialName("is_current")
    var isCurrent: Boolean,

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