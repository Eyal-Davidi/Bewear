package com.hva.hva_bewear.data.weather.network

import android.content.Context
import android.util.Log
import com.hva.hva_bewear.data.weather.network.response.WeatherResponse
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class WeatherService(private val getLocation: GetLocation) {
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

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private lateinit var location: Locations

    suspend fun getWeather(context: Context): WeatherResponse {
        location = getLocation.getLocation()
        // Directly call from the api
//        return client.get(url.replace(lat, location.lat.toString()).replace(lon, location.lon.toString()))

        // Use locally stored files to cache the api data
        val fileName = "${location.cityName.lowercase().replace(" ", "")}.json"
        var file = File(context.filesDir, fileName)
        // If the file does not yet exists a new file is created
        if (!file.exists()) file = createNewFile(file)
        //TODO make check if file is actually json

        var weather: WeatherResponse = json.decodeFromString(file.readText())
        // If the date in the file is before the current date the file is refreshed
        if (dateIsBeforeCurrentHour(weather.daily[0].date)) {
            writeApiDataToFile(file)
            weather = json.decodeFromString(file.readText())
        }
        return weather
    }

    private suspend fun createNewFile(file: File): File {
        kotlin.runCatching {
            file.createNewFile()
            writeApiDataToFile(file)
        }
        if (!file.exists()) throw FileNotFoundException()
        return file
    }

    private suspend fun writeApiDataToFile(file: File): File {
        Log.e("API_CALL", "writeApiDataToFile: An Api call has been made!")
        kotlin.runCatching {
            val printWriter = PrintWriter(file)
            //TODO make internet connection error
            val jsonFromApi: String = client.get(
                url.replace(lat, location.lat.toString()).replace(lon, location.lon.toString())
            )
            printWriter.write(jsonFromApi)
            printWriter.close()
        }
        return file
    }

    /**
     * Calculates if the provided dateInt is before the current hour
     */
    private fun dateIsBeforeCurrentHour(dateInt: Int): Boolean {
        val dateTime = dateInt.instantToDateTime()
        val now = LocalDateTime.now()
        return dateIsBeforeCurrentDay(dateInt) ||
                dateTime.hour < now.hour
    }

    /**
     * Calculates if the provided dateInt is before the current day
     */
    private fun dateIsBeforeCurrentDay(dateInt: Int): Boolean {
        val date = dateInt.instantToDate()
        return date.isBefore(LocalDate.now())
    }

    private fun Int.instantToDate(): LocalDate {
        return Instant.ofEpochSecond(toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun Int.instantToDateTime(): LocalDateTime {
        return Instant.ofEpochSecond(toLong())
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    companion object {
        private const val lat = "100000"
        private const val lon = "20000"
        private const val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lon}&exclude=minutely,current&units=metric&appid=***REMOVED***"
    }
}