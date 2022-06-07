package com.hva.bewear.data.weather.data.mapper

import com.hva.bewear.data.generic.toDateTime
import com.hva.bewear.data.weather.data.entity.DailyWeatherEntity
import com.hva.bewear.data.weather.data.entity.HourlyWeatherEntity
import com.hva.bewear.data.weather.data.entity.WeatherDetailsEntity
import com.hva.bewear.data.weather.data.entity.WeatherEntity
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.bewear.data.weather.network.response.WeatherDetailsResponse
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.domain.weather.model.DailyWeather
import com.hva.bewear.domain.weather.model.HourlyWeather
import com.hva.bewear.domain.weather.model.Weather
import com.hva.bewear.domain.weather.model.WeatherDetails
import java.time.Instant

object WeatherDataMapper {

    fun WeatherEntity.toDomain(): Weather {
        return Weather(
            lastUsed = lastUsed,
            cityName = cityName,
            isCurrent = isCurrent,
            timeZoneOffset = timeZoneOffset,
            daily = daily.map { it.toDomain() },
            hourly = hourly.map { it.toDomain(timeZoneOffset) },
        )
    }

    fun DailyWeatherEntity.toDomain(): DailyWeather {
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

    fun HourlyWeatherEntity.toDomain(timezoneOffset: Int): HourlyWeather {
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

    fun WeatherDetailsEntity.toDomain(): WeatherDetails {
        return WeatherDetails(id, main, description, icon)
    }

    fun WeatherResponse.toEntity(location: Location): WeatherEntity {
        return WeatherEntity(
            lastUsed = Instant.now(),
            cityName = location.cityName,
            state = location.state,
            country = location.country,
            isCurrent = location.isCurrent,
            timeZoneOffset = timeZoneOffset,
            lat = lat,
            lon = lon,
            daily = daily.map { it.toEntity() },
            hourly = hourly.map { it.toEntity() },
        )
    }

    fun DailyWeatherResponse.toEntity(): DailyWeatherEntity {
        return DailyWeatherEntity(
            date = date,
            temperature = DailyWeatherEntity.TemperatureDay(
                temperature.day,
                temperature.min,
                temperature.max,
                temperature.night,
                temperature.evening,
                temperature.morning
            ),
            feelsLike = DailyWeatherEntity.FeelsLike(
                feelsLike.day,
                feelsLike.night,
                feelsLike.evening,
                feelsLike.morning
            ),
            windSpeed = windSpeed,
            windDegree = windDegree,
            weather = weather.map { it.toEntity() },
            percentageOfPrecipitation = percentageOfPrecipitation,
            uvIndex = uvIndex,
            rain = rain,
        )
    }

    fun HourlyWeatherResponse.toEntity(): HourlyWeatherEntity {
        return HourlyWeatherEntity(
            date = date,
            temperature = temperature,
            feelsLike = feelsLike,
            uvIndex = uvIndex,
            windSpeed = windSpeed,
            windDegree = windDegree,
            weather = weather.map { it.toEntity() },
            percentageOfPrecipitation = percentageOfPrecipitation,
            rain = HourlyWeatherEntity.HourlyPrecipitation(rain.hour),
        )
    }

    fun WeatherDetailsResponse.toEntity(): WeatherDetailsEntity {
        return WeatherDetailsEntity(id, main, description, icon)
    }

    fun WeatherEntity.toLocation(): Location {
        return Location(
            cityName = cityName,
            state = state ?: "",
            country = country,
            lat = lat,
            lon = lon,
            lastUsed = lastUsed,
            isCurrent = isCurrent,
        )
    }

    fun WeatherEntity.refreshLastUsedAndIsCurrent(location: Location): WeatherEntity {
        return apply {
            lastUsed = Instant.now()
            isCurrent = location.isCurrent
        }
    }
}