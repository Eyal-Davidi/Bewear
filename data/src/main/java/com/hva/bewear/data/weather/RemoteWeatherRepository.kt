package com.hva.bewear.data.weather

import android.content.Context
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.weather.data.DataStore
import com.hva.bewear.data.weather.network.LocationData
import com.hva.bewear.data.weather.network.Locations
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherResponseMapper.toDomain
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val dataStore: WeatherDataStore,
) : WeatherRepository {

    override suspend fun getWeather(location: Location): Weather {
        return dataStore.getCachedWeather(location.cityName)?.takeIf {
            !it.lastUsed.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.also {
            dataStore.cacheData(it.refreshLastUsedAndIsCurrent(location))
        }?.toDomain() ?: service.getWeather(location).also {
            dataStore.cacheData(it.toEntity(location))
        }.toDomain(location)
    }


    private suspend fun setCoordinates(coordinates: Coordinates, cityName: String): LocationData {
        var location = LocationData()
        if (coordinates.lat != 0.0 && coordinates.lon != 0.0) {
            location.cityName = cityName
            location.lat = coordinates.lat
            location.lon = coordinates.lon
        } else {
            val loc = Locations.CityName(cityName)

            if (loc != Locations.EMPTY) {
                location = LocationData(loc.cityName, loc.lat, loc.lon)
            } else {

                locationService.locs.forEach {
                    val primarytext = it!!.getPrimaryText(StyleSpan(Typeface.BOLD)).toString()
                    if( primarytext == cityName){

                        locationService.returnLocation(primarytext).let{ locale ->
                            locale?.results?.forEach {
                                location =LocationData(primarytext, it!!.geometry.location.lat, it.geometry.location.lng)
                            }

                        }


                    }

                }
                if (location == LocationData()) {
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