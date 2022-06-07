package com.hva.bewear.domain.location.model

import java.time.Instant

data class Location(
    var cityName: String = "",
    var state: String = "",
    var country: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var lastUsed: Instant = Instant.MIN,
    var isCurrent: Boolean = false,
) {
    override fun toString(): String {
        return if(state.isBlank() && country.isBlank()) cityName
            else "$cityName, ${if (state.isNotBlank()) "$state," else ""} $country"
    }
}
