package com.hva.bewear.domain.location

import com.hva.bewear.domain.location.model.LocationData

class GetRecentLocations(private val repository: LocationRepository) {
    suspend operator fun invoke(): List<LocationData> {
        return repository.getRecentLocations()
    }
}