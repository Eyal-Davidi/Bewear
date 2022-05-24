package com.hva.bewear.data.weather.network

import android.content.Context
import android.util.Log
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.instantToDate
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.instantToDateTime
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.data.BuildConfig
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.location.response.Locale
import com.hva.bewear.domain.location.Coordinates
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
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class WeatherService(private val locationService: LocationService) {
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
    private lateinit var location: LocationData
    private var timeZoneOffset = 0

    suspend fun getWeather(
        context: Context,
        cityName: String,
        coordinates: Coordinates,
    ): WeatherResponse {

        val loc = Locations.CityName(cityName)

        setCoordinates(coordinates, cityName)
        // Directly call to the api
//        return getWeather(reason = "Api calls are always performed!")

        // Use locally stored files to cache the api data

        if (loc != Locations.EMPTY) {
            location = LocationData(loc.cityName, loc.lat, loc.lon)
        } else {
            var done = false
            locationService.places.forEach() {
                if (it.name+ ", " + it.state+ ", " + it.country == cityName && !done) {
                    location = LocationData(it.name, it.lat, it.lon)
                    done = true
                }
            }
            if (!done) {
                location = LocationData(
                    Locations.AMSTERDAM.cityName,
                    Locations.AMSTERDAM.lat,
                    Locations.AMSTERDAM.lon
                )
            }
        }


        val fileName = "${location.cityName.lowercase().replace(" ", "")}.json"
        var file = File(context.filesDir, fileName)

        // If the file does not yet exists a new file is created
        if (!file.exists()) {
            file = createNewFile(file)
            return writeApiDataToFile(file, "File did not exist")
        }

        // Checks if the file is actually json
        val weather: WeatherResponse = if (file.isJson()) json.decodeFromString(file.readText())
            else writeApiDataToFile(file, "File was not json")

        timeZoneOffset = weather.timeZoneOffset

        // If the date in the file is before the current date the file is refreshed
        return if (dateIsBeforeCurrentHour(weather.hourly[0].date)) {
            writeApiDataToFile(file, "File contained outdated data")
        } else weather
    }

    private fun setCoordinates(coordinates: Coordinates, cityName: String) {
        if (coordinates.lat != 0.0 && coordinates.lon != 0.0) {
            location.cityName = cityName
            location.lat = coordinates.lat
            location.lon = coordinates.lon
        } else location = Locations.CityName(cityName)
    }

    private fun createNewFile(file: File): File {
        kotlin.runCatching { file.createNewFile() }
        if (!file.exists()) throw FileNotFoundException()
        return file
    }

    private suspend fun writeApiDataToFile(file: File, reason: String): WeatherResponse {
        val jsonFromApi: String = getWeatherJson(reason)
        kotlin.runCatching {
            val printWriter = PrintWriter(file)
            printWriter.write(jsonFromApi)
            printWriter.close()
        }
        return json.decodeFromString(jsonFromApi)
    }

    private suspend fun getWeather(reason: String): WeatherResponse =
        json.decodeFromString(getWeatherJson(reason))

    private suspend fun getWeatherJson(reason: String): String {
        Log.e("API_CALL",
            "writeApiDataToFile: An Api call has been made! " +
                    "Location: ${location.cityName} because: $reason"
        )
        return client.get(url) {
            parameter("lat", location.lat)
            parameter("lon", location.lon)
            parameter("exclude", "minutely,current")
            parameter("units", "metric")
            parameter("appid", BuildConfig.OPENWEATHERMAP_KEY)
        }
    }

    /**
     * Calculates if the provided dateInt is before the current hour
     */
    private fun dateIsBeforeCurrentHour(dateInt: Int): Boolean {
        val dateTime = dateInt.instantToDateTime(timeZoneOffset)
        val now = LocalDateTime.now(
            ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timeZoneOffset))
        )
        return dateIsBeforeCurrentDay(dateInt) || dateTime.hour < now.hour
    }

    /**
     * Calculates if the provided dateInt is before the current day
     */
    private fun dateIsBeforeCurrentDay(dateInt: Int): Boolean {
        val date = dateInt.instantToDate()
        return date.isBefore(LocalDate.now())
    }

    private fun File.isJson(): Boolean {
        return try {
            json.decodeFromString<WeatherResponse>(readText())
            true
        } catch (exception: Exception) { false }
    }

    companion object {
        private const val url =
            "https://api.openweathermap.org/data/2.5/onecall"
    }
}
