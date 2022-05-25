package com.hva.bewear.data.weather.data

import android.content.Context
import com.hva.bewear.data.weather.network.LocationData
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.isBeforeCurrentHour
import com.hva.bewear.data.weather.network.response.CachingResponse
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.domain.weather.model.ApiCallReasons
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private var file = createFile()
    private var cachedLocations = getCachedLocations()

    fun shouldCallApi(location: LocationData): ApiCallReasons {
        if (!file.exists()) return ApiCallReasons.LOCATION_DID_NOT_EXIST

        val weather: WeatherResponse
        if (!file.isJson()) return ApiCallReasons.FILE_WAS_NOT_JSON
        else weather = getCachedData(location = location)
            ?: return ApiCallReasons.LOCATION_DID_NOT_EXIST

        return if (
            Instant.ofEpochSecond(weather.hourly[0].date.toLong())
                .isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(weather.timeZoneOffset))
        ) ApiCallReasons.LOCATION_WAS_OUTDATED
        else ApiCallReasons.NORMAL
    }

    fun cacheData(weather: WeatherResponse, locationName: String): WeatherResponse {
        weather.apply {
            created = LocalDateTime.now().toEpochSecond(
                    ZoneOffset.ofTotalSeconds(weather.timeZoneOffset)
                ).toInt()
            cityName = locationName
        }

        val list = arrayListOf(weather)
        list.addAll(cachedLocations.filter {
            it.cityName != locationName
        })
        list.sortBy { it.created }
        file.writeDataToFile(
            json.encodeToJsonElement(CachingResponse(list)).toString()
        )
        return weather
    }

    fun getCachedData(location: LocationData): WeatherResponse? {
        cachedLocations = getCachedLocations()
        return cachedLocations.find { it.cityName == location.cityName }
    }

    private fun getCachedLocations(): List<WeatherResponse> {
        return try {
            json.decodeFromString<CachingResponse>(file.readText()).cachedLocations
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun File.writeDataToFile(jsonString: String): Boolean {
        return try {
            val printWriter = PrintWriter(this)
            printWriter.write(jsonString)
            printWriter.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun createFile(): File {
        val file = File(context.filesDir, "weather-location-cache.json")
        return if (file.exists()) file
        else file.newFile()
    }

    private fun File.newFile(): File {
        kotlin.runCatching { createNewFile() }
        if (!exists()) throw FileNotFoundException()
        return this
    }

    private fun File.isJson(): Boolean {
        return try {
            json.decodeFromString<CachingResponse>(readText())
            true
        } catch (exception: Exception) {
            false
        }
    }
}