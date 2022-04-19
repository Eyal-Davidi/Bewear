package com.hva.hva_bewear.domain.weather

import com.hva.hva_bewear.domain.weather.model.Locations
var location: Locations = Locations.AMSTERDAM
var set = arrayListOf<String>()
class LocationPicker() {


    fun setOfLocations(): ArrayList<String> {
        if (set.isEmpty()) {
            set.add(location.cityName)
            enumValues<Locations>().forEach {
                if (it.cityName != location.cityName) {
                    set.add(it.cityName)
                }
            }
        }
        return set
    }
        fun calLocation(): ArrayList<Double> {
            return arrayListOf(location.lat, location.lon)
        }

        fun setLocation(locale: String) {
            location = Locations.CityName(locale)
        }

}