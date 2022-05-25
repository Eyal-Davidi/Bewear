package com.hva.bewear.data.weather.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachingResponse(
    @SerialName("locations")
    val cachedLocations: List<WeatherResponse>
)