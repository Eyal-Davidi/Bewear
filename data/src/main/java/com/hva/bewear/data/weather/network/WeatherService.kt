package com.hva.bewear.data.weather.network

import android.util.Log
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.data.BuildConfig
import com.hva.bewear.domain.weather.model.ApiCallReasons
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

    suspend fun getWeather(location:LocationData): WeatherResponse {
        Log.e(
            API_LOG_TAG,
            "writeApiDataToFile: An Api call has been made! Location: ${location.cityName}"
        )
        return client.get(URL) {
            parameter("lat", location.lat)
            parameter("lon", location.lon)
            parameter("exclude", "minutely,current")
            parameter("units", "metric")
            parameter("appid", BuildConfig.OPENWEATHERMAP_KEY)
        }
    }

    companion object {
        private const val URL = "https://api.openweathermap.org/data/2.5/onecall"
        private const val API_LOG_TAG = "API_CALL"
    }
}
