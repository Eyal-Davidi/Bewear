package com.hva.bewear.domain.location.model

import java.time.Instant

data class Location(
    var cityName: String = "",
    var fullName: String = "",
    var placeId : String = "",
    var lat : Double? = null,
    var lon : Double? = null,
    var lastUsed: Instant = Instant.MIN,
    var isCurrent: Boolean = false,
) {
    object SetFullName{
        operator fun invoke (cityName: String, state : String, country : String ) : String  {
            return if(state.isBlank() && country.isBlank()) cityName
            else "$cityName, ${if (state.isNotBlank()) "$state," else ""} $country"
        }
    }

    override fun toString(): String {
        return fullName
    }

}
