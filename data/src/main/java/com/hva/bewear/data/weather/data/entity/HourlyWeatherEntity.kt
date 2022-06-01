package com.hva.bewear.data.weather.data.entity

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class HourlyWeatherEntity(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    val date: Instant,
    @SerialName("temp")
    val temperature: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("uvi")
    val uvIndex: Double,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDegree: Int,
    @SerialName("weather")
    val weather: List<WeatherDetailsEntity>,
    @SerialName("pop")
    val percentageOfPrecipitation: Double,
    @SerialName("rain")
    val rain: HourlyPrecipitation = HourlyPrecipitation(),
) {
    @Serializable
    data class HourlyPrecipitation(
        @SerialName("1h")
        val hour: Double = 0.0
    )
}
