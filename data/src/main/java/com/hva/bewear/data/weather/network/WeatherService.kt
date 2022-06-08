package com.hva.bewear.data.weather.network

import android.util.Log
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.data.BuildConfig
import com.hva.bewear.domain.location.model.Location
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

class WeatherService {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
            acceptContentTypes = listOf(
                ContentType.Application.Json,
                ContentType.Application.FormUrlEncoded
            )
        }
    }

    suspend fun getWeather(location: Location): WeatherResponse {
        Log.e(
            API_LOG_TAG,
            "WeatherService.kt: An Api call has been made! Location: ${location.cityName}"
        )
        Log.e(
            API_LOG_TAG,
            location.toString()
        )
        return client.get<WeatherResponse>(URL) {
            parameter("lat", location.lat?: 0.0)
            parameter("lon", location.lon?: 0.0)
            parameter("exclude", "minutely,current")
            parameter("units", "metric")
            parameter("appid", BuildConfig.OPENWEATHERMAP_KEY)
        }.also {
            Log.e(
                API_LOG_TAG, it.toString()
            )
        }
    }

    companion object {
        private const val URL = "https://api.openweathermap.org/data/2.5/onecall"
        private const val API_LOG_TAG = "API_CALL"
    }
}
