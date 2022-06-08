package com.hva.bewear.data.weather

import android.content.Context
import com.hva.bewear.data.generic.isBeforeCurrentHour
import com.hva.bewear.data.location.LocationService
import com.hva.bewear.data.weather.data.WeatherDataStore
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.refreshLastUsedAndIsCurrent
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toDomain
import com.hva.bewear.data.weather.data.mapper.WeatherDataMapper.toEntity
import com.hva.bewear.data.weather.network.WeatherService
import com.hva.bewear.data.weather.network.mapper.WeatherResponseMapper.toDomain
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.domain.weather.data.WeatherRepository
import com.hva.bewear.domain.weather.model.Weather
import java.time.ZoneOffset

class RemoteWeatherRepository(
    private val service: WeatherService,
    private val dataStore: WeatherDataStore,
    private val locationService: LocationService,
) : WeatherRepository {

    override suspend fun getWeather(location: Location): Weather {
        var loc = location
        if(location.lat == null|| location.lon == null){
             loc = locationService.returnLocation(location)
        }

        return dataStore.getCachedWeather(loc.cityName)?.takeIf {
            !it.lastUsed.isBeforeCurrentHour(ZoneOffset.ofTotalSeconds(it.timeZoneOffset))
        }?.also {
            dataStore.cacheData(it.refreshLastUsedAndIsCurrent(loc))
        }?.toDomain() ?: service.getWeather(loc).also {
            dataStore.cacheData(it.toEntity(loc))
        }.toDomain(loc)
    }

/*
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
    }*/
}