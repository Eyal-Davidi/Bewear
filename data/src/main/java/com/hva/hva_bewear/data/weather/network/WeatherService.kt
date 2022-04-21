package com.hva.hva_bewear.data.weather.network

import android.os.Environment
import android.util.Log
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import com.hva.hva_bewear.domain.weather.LocationPicker
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.util.*
import kotlin.io.path.toPath
import kotlin.math.log


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

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun getWeather(): WeatherResponse {
        val location = locationPicker.calLocation()
//        return client.get(url.replace(lat, location.lat.toString()).replace(lon, location.lon.toString()))

//        val file = File("/data/app/~~EW7KV06MeF0GG8kgNzXl3w==/com.hva.appname-6xv0VSSwsUhYX9RRqc7U0Q==/base.apk!/res/raw/", "Beijing.json")
//        file.createNewFile()
//        file.appendText(client.get(url.replace(lat, location.lat.toString()).replace(lon, location.lon.toString())))

//        val file = File.createTempFile("${Environment.getDataDirectory().absolutePath}/", "beijing.json")
//        file.appendText(client.get(url.replace(lat, location.lat.toString()).replace(lon, location.lon.toString())))


        val fileName = "res/raw/${location.cityName.lowercase()}.json"
        val classLoader = this.javaClass.classLoader!!
        Log.i("TAG", "getWeather: ${Environment.getDataDirectory().absolutePath}")
        val bufferedReader: BufferedReader = classLoader.getResourceAsStream(fileName).bufferedReader()
        val jsonString = bufferedReader.use(BufferedReader::readText)
        return json.decodeFromString(jsonString)

//        return json.decodeFromString(file.readText())
    }

    companion object {
        private const val lat ="100000"
        private const val lon ="20000"
        private const val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lon}&exclude=minutely,current&units=metric&appid=***REMOVED***"
    }
}