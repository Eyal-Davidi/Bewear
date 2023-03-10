package com.hva.bewear.data.weather.network.response

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class HourlyWeatherResponse(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    val date: Instant,
    @SerialName("temp")
    val temperature: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("uvi")
    val uvIndex: Double,
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("visibility")
    val visibility: Int,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDegree: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    @SerialName("weather")
    val weather: List<WeatherDetailsResponse>,
    @SerialName("pop")
    val percentageOfPrecipitation: Double,
    @SerialName("rain")
    val rain: HourlyPrecipitation = HourlyPrecipitation(),
    @SerialName("snow")
    val snow: HourlyPrecipitation = HourlyPrecipitation(),
) {
    @Serializable
    data class HourlyPrecipitation(
        @SerialName("1h")
        val hour: Double = 0.0
    )
}
