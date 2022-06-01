package com.hva.bewear.domain.location

import com.hva.bewear.domain.location.model.LocationData

interface LocationRepository {
    suspend fun getLocation(text : String): List<LocationData>
    suspend fun getRecentLocations(): List<LocationData>
}