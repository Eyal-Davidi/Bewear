package com.hva.bewear.data.weather.network.response

import com.hva.bewear.data.weather.network.serializer.InstantAsLongSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class DailyWeatherResponse(
    @SerialName("dt")
    @Serializable(with = InstantAsLongSerializer::class)
    val date: Instant,
    @SerialName("sunrise")
    val sunrise: Int,
    @SerialName("sunset")
    val sunset: Int,
    @SerialName("moonrise")
    val moonrise: Int,
    @SerialName("moonset")
    val moonset: Int,
    @SerialName("moon_phase")
    val moonPhase: Double,
    @SerialName("temp")
    val temperature: TemperatureDay,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDegree: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    @SerialName("weather")
    val weather: List<WeatherDetailsResponse>,
    @SerialName("clouds")
    val clouds: Int,
    @SerialName("pop")
    val percentageOfPrecipitation: Double,
    @SerialName("uvi")
    val uvIndex: Double,
    @SerialName("rain")
    val rain: Double = 0.0,
    @SerialName("snow")
    val snow: Double = 0.0,
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
