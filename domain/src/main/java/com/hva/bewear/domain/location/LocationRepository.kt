package com.hva.bewear.domain.location

import com.hva.bewear.domain.location.model.Location

interface LocationRepository {
    suspend fun getLocation(text : String): List<Location>
    suspend fun getRecentLocations(): List<Location>
}