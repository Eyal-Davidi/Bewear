package com.hva.bewear.data.weather.data.entity

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DailyWeatherEntity(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    val date: Instant,
    @SerialName("temp")
    val temperature: TemperatureDay,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDegree: Int,
    @SerialName("weather")
    val weather: List<WeatherDetailsEntity>,
    @SerialName("pop")
    val percentageOfPrecipitation: Double,
    @SerialName("uvi")
    val uvIndex: Double,
    @SerialName("rain")
    val rain: Double = 0.0,
) {

    @Serializable
    data class TemperatureDay(
        @SerialName("day")
        val day: Double,
        @SerialName("min")
        val min: Double,
        @SerialName("max")
        val max: Double,
        @SerialName("night")
        val night: Double,
        @SerialName("eve")
        val evening: Double,
        @SerialName("morn")
        val morning: Double
    )

    @Serializable
    data class FeelsLike(
        @SerialName("day")
        val day: Double,
        @SerialName("night")
        val night: Double,
        @SerialName("eve")
        val evening: Double,
        @SerialName("morn")
        val morning: Double
    )
}
