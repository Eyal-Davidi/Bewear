package com.hva.hva_bewear.domain.weather

import com.hva.hva_bewear.domain.weather.model.Locations

class LocationPicker() {
    var location: Locations = Locations.NORGE


    fun setOfLocations(): ArrayList<String>{
        val set = arrayListOf<String>()
        set.add(location.cityName)
        enumValues<Locations>().forEach { if(it.cityName!=location.cityName){set.add(it.cityName)} }
        return set
    }

    fun calLocation(): ArrayList<Double> {
        return arrayListOf(location.lat,location.lon)
    }

    fun setLocation(locale: String) {
        location = Locations.CityName(locale)
    }
}