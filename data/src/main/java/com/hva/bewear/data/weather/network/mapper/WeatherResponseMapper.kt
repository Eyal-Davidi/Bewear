package com.hva.bewear.data.weather.network.mapper

import com.hva.bewear.data.generic.toDateTime
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.bewear.data.weather.network.response.WeatherDetailsResponse
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.domain.weather.model.DailyWeather
import com.hva.bewear.domain.weather.model.HourlyWeather
import com.hva.bewear.domain.weather.model.Weather
import com.hva.bewear.domain.weather.model.WeatherDetails

object WeatherResponseMapper {
    fun WeatherResponse.toDomain(location: Location): Weather {
        return Weather(
            lastUsed = hourly.first().date,
            cityName = location.cityName,
            isCurrent = location.isCurrent,
            timeZoneOffset = timeZoneOffset,
            daily = daily.map { it.toDomain() },
            hourly = hourly.map { it.toDomain(timeZoneOffset) },
        )
    }

    fun DailyWeatherResponse.toDomain(): DailyWeather {
        return DailyWeather(
            date = date.toDateTime(),
            temperature = DailyWeather.TemperatureDay(
                temperature.day,
                temperature.min,
                temperature.max,
                temperature.night,
                temperature.evening,
                temperature.morning
            ),
            feelsLike = DailyWeather.FeelsLike(
                feelsLike.day,
                feelsLike.night,
                feelsLike.evening,
                feelsLike.morning
            ),
            windSpeed = windSpeed,
            windDegree = windDegree,
            weather = weather.map { it.toDomain() },
            percentageOfPrecipitation = percentageOfPrecipitation,
            uvIndex = uvIndex,
            rain = rain,
        )
    }

    fun HourlyWeatherResponse.toDomain(timezoneOffset: Int): HourlyWeather {
        return HourlyWeather(
            date = date.toDateTime(timezoneOffset),
            temperature = temperature,
            feelsLike = feelsLike,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDegree = windDegree,
            weather = weather.map { it.toDomain() },
            percentageOfPrecipitation = percentageOfPrecipitation,
            rain = HourlyWeather.HourlyPrecipitation(rain.hour),
        )
    }

    fun WeatherDetailsResponse.toDomain(): WeatherDetails {
        return WeatherDetails(id, main, description, icon)
    }
}