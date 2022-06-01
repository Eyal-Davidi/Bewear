package com.hva.bewear.data.weather

import android.content.Context
import com.hva.bewear.data.generic.isBeforeCurrentHour
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.weather.data.DataStore
import com.hva.bewear.data.weather.network.LocationData
import com.hva.bewear.data.weather.network.Locations
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toDomain
import com.hva.bewear.data.weather.network.mapper.WeatherMapper.toEntity
import com.hva.bewear.data.weather.network.response.WeatherResponse
import com.hva.bewear.domain.location.Coordinates
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.ApiCallReasons
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val locationService: LocationService,
    context: Context,
) : WeatherRepository {
    private val dataStore = DataStore(context)

    override suspend fun getWeather(cityName: String, coordinates: Coordinates): Weather {
        val location = setCoordinates(coordinates, cityName)

        return dataStore.getCachedData(cityName)?.takeIf {
            !it.created.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.toDomain() ?: service.getWeather(location).also {
            dataStore.cacheData(it.toEntity(location))
        }.toDomain(location)
    }








    private fun setCoordinates(coordinates: Coordinates, cityName: String): LocationData {
        var location = LocationData()
        if (coordinates.lat != 0.0 && coordinates.lon != 0.0) {
            location.cityName = cityName
            location.lat = coordinates.lat
            location.lon = coordinates.lon
            location.isCurrent = true
        } else {
            val loc = Locations.CityName(cityName)

            if (loc != Locations.EMPTY) {
                location = LocationData(loc.cityName, loc.lat, loc.lon)
            } else {
                var done = false
                locationService.places.forEach {
                    if (it.name + ", " + it.state + ", " + it.country == cityName && !done) {
                        location = LocationData(it.name, it.lat, it.lon)
                        done = true
                    }
                }
                if (!done) {
                    location = LocationData(
                        Locations.AMSTERDAM.cityName,
                        Locations.AMSTERDAM.lat,
                        Locations.AMSTERDAM.lon
                    )
                }
            }
        }
        return location
    }
}