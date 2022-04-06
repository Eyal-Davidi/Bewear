package com.hva.hva_bewear.data.weather

import com.hva.hva_bewear.data.weather.network.WeatherService
import com.hva.hva_bewear.data.weather.network.response.DailyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.HourlyWeatherResponse
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.domain.weather.model.HourlyWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.domain.weather.model.WeatherDetails

class RemoteWeatherRepository(private val service: WeatherService) : WeatherRepository {

    override suspend fun getWeather(): Weather {
        return parseWeatherResponse(service.getWeather())
    }

    private fun parseWeatherResponse(response: WeatherResponse):Weather {
        return Weather(
            daily = parseDailyResponse(response.daily),
            hourly = parseHourlyResponse(response.hourly)
        )
    }

    private fun parseDailyResponse(response: List<DailyWeatherResponse>): List<DailyWeather> {
        val dailyList = arrayListOf<DailyWeather>()
        response.forEach {
            val weatherDetails = arrayListOf<WeatherDetails>()
            it.weather.forEach { details ->
                weatherDetails.add(
                    WeatherDetails(details.id, details.main, details.description, details.icon)
                )
            }
            dailyList.add(
                DailyWeather(
                    date = it.date,
                    sunrise = it.sunrise,
                    sunset = it.sunset,
                    moonrise = it.moonrise,
                    moonset = it.moonset,
                    moonPhase = it.moonPhase,
                    temperature = DailyWeather.TemperatureDay(
                        it.temperature.day,
                        it.temperature.min,
                        it.temperature.max,
                        it.temperature.night,
                        it.temperature.evening,
                        it.temperature.morning
                    ),
                    feelsLike = DailyWeather.FeelsLike(
                        it.feelsLike.day,
                        it.feelsLike.night,
                        it.feelsLike.evening,
                        it.feelsLike.morning
                    ),
                    pressure = it.pressure,
                    humidity = it.humidity,
                    dewPoint = it.dewPoint,
                    windSpeed = it.windSpeed,
                    windDegree = it.windDegree,
                    windGust = it.windGust,
                    weather = weatherDetails,
                    clouds = it.clouds,
                    percentageOfPrecipitation = it.percentageOfPrecipitation,
                    uvIndex = it.uvIndex,
                    rain = it.rain,
                    snow = it.snow,
                )
            )
        }
        return dailyList
    }

    private fun parseHourlyResponse(response: List<HourlyWeatherResponse>): List<HourlyWeather> {
        val hourlyWeather = arrayListOf<HourlyWeather>()
        response.forEach {
            val weatherDetails = arrayListOf<WeatherDetails>()
            it.weather.forEach { details ->
                weatherDetails.add(
                    WeatherDetails(details.id, details.main, details.description, details.icon)
                )
            }
            hourlyWeather.add(
                HourlyWeather(
                    date = it.date,
                    temperature = it.temperature,
                    feelsLike = it.feelsLike,
                    pressure = it.pressure,
                    humidity = it.humidity,
                    dewPoint = it.dewPoint,
                    uvIndex = it.uvIndex,
                    clouds = it.clouds,
                    visibility = it.visibility,
                    windSpeed = it.windSpeed,
                    windDegree = it.windDegree,
                    windGust = it.windGust,
                    weather = weatherDetails,
                    percentageOfPrecipitation = it.percentageOfPrecipitation,
                    rain = HourlyWeather.HourlyPrecipitation(it.rain.hour),
                    snow = HourlyWeather.HourlyPrecipitation(it.snow.hour),
                )
            )
        }
        return hourlyWeather
    }
}