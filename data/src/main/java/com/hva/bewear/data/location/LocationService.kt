package com.hva.bewear.data.location


import Locale
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks.await
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.hva.bewear.data.BuildConfig
import com.hva.bewear.domain.location.model.Location
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class LocationService(val context: Context) {
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
    private var placesClient: PlacesClient? = null

    //lateinit var places: List<Locale_dep>
    var locs: List<AutocompletePrediction?> = emptyList()
     fun update(text: String): List<AutocompletePrediction?>? {

        val token = AutocompleteSessionToken.newInstance()
        if (placesClient == null) {
            Places.initialize(context, BuildConfig.GOOGLEAPI_KEY)
            placesClient = Places.createClient(context)
        }


        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(text)
                .build()


        val results: Task<FindAutocompletePredictionsResponse> =
            placesClient!!.findAutocompletePredictions(request)

        //Wait to get results.

        //Wait to get results.
        try {
            await(results, 1, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        return if (results.isSuccessful) {
            if (results.result != null) {
                locs = results.result.autocompletePredictions
            Log.e("Help", locs.toString())
                locs
            } else null

        } else null



        /* places =
             json.decodeFromString(
                 client.get(
                     "http://api.openweathermap.org/geo/1.0/direct?q=" +
                             text + "&limit=5&appid=" +
                             "***REMOVED***"

                 )
             )
         return places*/
    }


    suspend fun returnLocation(text: Location): Location {


                val locale : Locale = json.decodeFromString(
                    client.get(
                        "https://maps.googleapis.com/maps/api/geocode/json?place_id=" + text.placeId + "&key=" + BuildConfig.GOOGLEAPI_KEY

                    )
                )
        return text.copy(lat = locale.results?.get(0)?.geometry?.location?.lat, lon = locale.results?.get(0)?.geometry?.location?.lng)
    }
}

