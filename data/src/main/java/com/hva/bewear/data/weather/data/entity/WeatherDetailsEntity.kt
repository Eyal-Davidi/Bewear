package com.hva.bewear.data.weather.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailsEntity(
    @SerialName("id")
    val id : Int,
    @SerialName("main")
    val main : String,
    @SerialName("description")
    val description : String,
    @SerialName("icon")
    val icon : String
)