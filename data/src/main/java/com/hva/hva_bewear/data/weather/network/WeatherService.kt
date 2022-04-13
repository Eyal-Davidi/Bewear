package com.hva.hva_bewear.data.weather.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import com.hva.hva_bewear.domain.weather.LocationPicker

class WeatherService {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
            acceptContentTypes =
                listOf(ContentType.Application.Json, ContentType.Application.FormUrlEncoded)
        }
    }
    private val locationPicker: LocationPicker = LocationPicker()

    suspend fun getWeather(): WeatherResponse {
        val location: ArrayList<Double> = locationPicker.calLocation()
        return client.get(url.replace(lat, location[0].toString()).replace(lon, location[1].toString()))
    }

    companion object {
        private const val lat ="100000"
        private const val lon ="20000"
        private const val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lon}&exclude=minutely,current&units=metric&appid=***REMOVED***"
    }
}