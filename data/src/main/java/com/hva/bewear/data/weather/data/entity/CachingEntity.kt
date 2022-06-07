package com.hva.bewear.data.weather.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CachingEntity(
    @SerialName("locations")
    val cachedLocations: List<WeatherEntity>
)