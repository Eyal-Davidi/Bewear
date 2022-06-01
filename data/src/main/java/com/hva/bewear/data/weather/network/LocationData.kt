package com.hva.bewear.data.weather.network

data class LocationData(
    var cityName: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var isCurrent: Boolean = false,
)
