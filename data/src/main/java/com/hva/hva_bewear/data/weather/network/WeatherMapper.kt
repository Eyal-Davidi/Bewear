package com.hva.hva_bewear.data.weather.network

import com.hva.hva_bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.WeatherDetailsResponse
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.HourlyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.domain.weather.model.WeatherDetails
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object WeatherMapper {
    fun WeatherResponse.toDomain(): Weather {
        return Weather(
            daily = this.daily.map { it.toDomain() },
            hourly = this.hourly.map { it.toDomain() },
        )
    }

    fun DailyWeatherResponse.toDomain(): DailyWeather {
        return DailyWeather(
            date = this.date.instantToDateTime(),
            sunrise = this.sunrise,
            sunset = this.sunset,
            moonrise = this.moonrise,
            moonset = this.moonset,
            moonPhase = this.moonPhase,
            temperature = DailyWeather.TemperatureDay(
                this.temperature.day,
                this.temperature.min,
                this.temperature.max,
                this.temperature.night,
                this.temperature.evening,
                this.temperature.morning
            ),
            feelsLike = DailyWeather.FeelsLike(
                this.feelsLike.day,
                this.feelsLike.night,
                this.feelsLike.evening,
                this.feelsLike.morning
            ),
            pressure = this.pressure,
            humidity = this.humidity,
            dewPoint = this.dewPoint,
            windSpeed = this.windSpeed,
            windDegree = this.windDegree,
            windGust = this.windGust,
            weather = this.weather.map { it.toDomain() },
            clouds = this.clouds,
            percentageOfPrecipitation = this.percentageOfPrecipitation,
            uvIndex = this.uvIndex,
            rain = this.rain,
            snow = this.snow,
        )
    }

    fun HourlyWeatherResponse.toDomain(): HourlyWeather {
        return HourlyWeather(
            date = this.date.instantToDateTime(),
            temperature = this.temperature,
            feelsLike = this.feelsLike,
            pressure = this.pressure,
            humidity = this.humidity,
            dewPoint = this.dewPoint,
            uvIndex = this.uvIndex,
            clouds = this.clouds,
            visibility = this.visibility,
            windSpeed = this.windSpeed,
            windDegree = this.windDegree,
            windGust = this.windGust,
            weather = this.weather.map { it.toDomain() },
            percentageOfPrecipitation = this.percentageOfPrecipitation,
            rain = HourlyWeather.HourlyPrecipitation(this.rain.hour),
            snow = HourlyWeather.HourlyPrecipitation(this.snow.hour),
        )
    }

    fun WeatherDetailsResponse.toDomain():WeatherDetails {
        return WeatherDetails(this.id, this.main, this.description, this.icon)
    }

    fun Int.instantToDate(): LocalDate {
        return Instant.ofEpochSecond(toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun Int.instantToDateTime(): LocalDateTime {
        return Instant.ofEpochSecond(toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}