package com.hva.hva_bewear.domain.weather

import com.hva.hva_bewear.domain.weather.model.Locations

object LocationPicker {
    var location: Locations = Locations.AMSTERDAM
    var set = arrayListOf<String>()

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
        fun calLocation(): Locations {
            return location
        }

        fun setLocation(locale: String) {
            location = Locations.CityName(locale)
        }

}