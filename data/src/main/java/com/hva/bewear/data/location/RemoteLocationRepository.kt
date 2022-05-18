package com.hva.bewear.data.location

import com.hva.bewear.domain.location.LocationRepository

class RemoteLocationRepository(val locationService: LocationService) : LocationRepository{
    override suspend fun getLocation(text: String): List<String> {
        val location = ArrayList<String>()

            locationService.update(text).forEach { location.add(it.name) }
        return location
    }
}