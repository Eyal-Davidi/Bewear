package com.hva.bewear.data.weather.network

import com.hva.bewear.domain.location.LocationPicker

class ListOfLocations : LocationPicker {

    var set = arrayListOf<String>()

    override fun setOfLocations(): List<String> {
        enumValues<Locations>().forEach {
            set.add(it.cityName)
        }
        return set
    }
}