package com.hva.bewear.domain.weather.model

data class WeatherDetails(
    val id : Int,
    val main : String,
    val description : String,
    val icon : String
)
