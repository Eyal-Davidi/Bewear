package com.hva.bewear.data.weather.network.mapper

import com.hva.bewear.data.generic.toDateTime
import com.hva.bewear.data.weather.data.entity.DailyWeatherEntity
import com.hva.bewear.data.weather.data.entity.HourlyWeatherEntity
import com.hva.bewear.data.weather.data.entity.WeatherDetailsEntity
import com.hva.bewear.data.weather.data.entity.WeatherEntity
import com.hva.bewear.domain.location.model.LocationData
import com.hva.bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.bewear.data.weather.network.response.WeatherDetailsResponse
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.domain.weather.model.DailyWeather
import com.hva.bewear.domain.weather.model.HourlyWeather
import com.hva.bewear.domain.weather.model.Weather
import com.hva.bewear.domain.weather.model.WeatherDetails

object WeatherMapper {
    fun WeatherResponse.toDomain(location: LocationData): Weather {
        return Weather(
            created = hourly.first().date,
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

    fun WeatherEntity.toDomain(): Weather {
        return Weather(
            created = created,
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

    fun WeatherResponse.toEntity(location: LocationData): WeatherEntity {
        return WeatherEntity(
            created = hourly.first().date,
            cityName = location.cityName,
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

    fun WeatherEntity.toLocation(): LocationData {
        return LocationData(
            cityName = cityName,
            lat = lat,
            lon = lon,
            isCurrent = isCurrent,
        )
    }
}