package com.hva.bewear.data.location

import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toLocation
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.domain.location.LocationRepository

class RemoteLocationRepository(
    private val locationService: LocationService,
    private val dataStore: WeatherDataStore,
) : LocationRepository {
    override suspend fun getLocation(text: String): List<Location> {
        val location = ArrayList<Location>()

        locationService.update(text).forEach {
            location.add(
                Location(
                    cityName = it.name,
                    state = it.state ?: "",
                    country = it.country,
                    lat = it.lat,
                    lon = it.lon,
                )
            )
        }
        return location
    }

    override suspend fun getRecentLocations(): List<Location> {
        return dataStore.getCachedLocations().map { it.toLocation() }
    }
}