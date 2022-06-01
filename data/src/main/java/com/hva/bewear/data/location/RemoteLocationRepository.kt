package com.hva.bewear.data.location

import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toLocation
import com.hva.bewear.domain.location.model.LocationData
import com.hva.bewear.domain.location.LocationRepository

class RemoteLocationRepository(
    private val locationService: LocationService,
    private val dataStore: WeatherDataStore,
) : LocationRepository {
    override suspend fun getLocation(text: String): List<LocationData> {
        val location = ArrayList<LocationData>()

        locationService.update(text).forEach {
            location.add(
                LocationData(it.name,it.lat, it.lon, false,it.state ?: "", it.country, )
            )
        }
        return location
    }

    override suspend fun getRecentLocations(): List<LocationData> {
        return dataStore.getCachedLocations().map { it.toLocation() }
    }
}