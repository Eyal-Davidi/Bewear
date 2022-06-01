package com.hva.bewear.domain.location.model

import java.time.Instant

data class Location(
    var cityName: String = "",
    var state: String? = null,
    var country: String = "",
    var lat : Double = 0.0,
    var lon : Double = 0.0,
    var lastUsed: Instant = Instant.MIN,
    var isCurrent: Boolean = false,
) {
    override fun toString(): String {
        return "$cityName, ${if (state != null) "$state," else ""} $country"
    }
}
