package com.hva.bewear.domain.location

import com.hva.bewear.domain.location.model.Location

class GetRecentLocations(private val repository: LocationRepository) {
    suspend operator fun invoke(): List<Location> {
        return repository.getRecentLocations()
    }
}