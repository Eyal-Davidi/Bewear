package com.hva.bewear.domain.location.model

data class LocationData(
    var cityName: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var isCurrent: Boolean = false,
    var state: String = "",
    var country: String = "",
)
