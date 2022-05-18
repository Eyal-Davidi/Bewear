package com.hva.bewear.data.weather.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDetailsResponse(
    @SerialName("id")
    val id : Int,
    @SerialName("main")
    val main : String,
    @SerialName("description")
    val description : String,
    @SerialName("icon")
    val icon : String
)