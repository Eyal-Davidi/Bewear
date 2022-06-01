package com.hva.bewear.data.weather.data

import android.content.Context
import android.util.Log
import com.hva.bewear.data.weather.data.entity.CachingEntity
import com.hva.bewear.data.weather.data.entity.WeatherEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter

class DataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private var file = createFile()

    fun cacheData(weather: WeatherEntity) {
        val list = arrayListOf(weather)
        list.addAll(getCachedLocations().filter {
            it.cityName != weather.cityName
        })
        list.sortBy { it.created }
        file.writeDataToFile(json.encodeToJsonElement(CachingEntity(list)))
    }

    fun getCachedData(cityName: String): WeatherEntity? {
        return getCachedLocations().find { it.cityName == cityName }
    }

    private fun getCachedLocations(): List<WeatherEntity> {
        return try {
            json.decodeFromString<CachingEntity>(file.readText()).cachedLocations
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun File.writeDataToFile(json: JsonElement): Boolean {
        return try {
            val printWriter = PrintWriter(this)
            printWriter.write(json.toString())
            printWriter.close()
            true
        } catch (e: Exception) {
            Log.e("CachingError", "writeDataToFile: $e", )
            false
        }
    }

    private fun createFile(): File {
        val file = File(context.filesDir, CACHE_FILE_NAME)
        return if (file.exists()) file
        else {
            kotlin.runCatching { file.createNewFile() }
            if (!file.exists()) throw FileNotFoundException()
            else file
        }
    }

    companion object {
        private const val CACHE_FILE_NAME = "weather-location-cache.json"
    }
}