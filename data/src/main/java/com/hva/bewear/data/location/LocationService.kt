package com.hva.bewear.data.location


import com.hva.bewear.data.location.response.Locale
import io.ktor.client.request.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString

class LocationService {
    private val json =
        kotlinx.serialization.json.Json { ignoreUnknownKeys = true; isLenient = true }

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
    lateinit var places: List<Locale>
     suspend fun update(text: String): List<Locale> {

        places =
            json.decodeFromString(
                client.get(
                    "http://api.openweathermap.org/geo/1.0/direct?q=" +
                            text + "&limit=5&appid=" +
                            "***REMOVED***"
                )
            )
        return places
    }


}
