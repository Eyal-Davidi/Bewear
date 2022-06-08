package com.hva.bewear.domain.location.model

import java.time.Instant

data class Location(
    val cityName: String,
    val fullName: String,
    val placeId: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val lastUsed: Instant = Instant.MIN,
    val isCurrent: Boolean = false,
) {
    override fun toString(): String {
        return fullName
    }
}
