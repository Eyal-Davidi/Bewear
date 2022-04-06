package com.hva.hva_bewear.data.weather.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse

class WeatherService {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
            acceptContentTypes = listOf(ContentType.Application.Json, ContentType.Application.FormUrlEncoded)
        }
    }

    suspend fun getWeather(): WeatherResponse {
        return client.get(url)
    }

    companion object {
        private const val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=52.3676&lon=4.9041&exclude=minutely,current&units=metric&appid=***REMOVED***"
    }
}