package com.hva.hva_bewear.domain.weather

import com.hva.hva_bewear.domain.weather.model.Locations
import com.hva.hva_bewear.domain.weather.data.LocationPickerInterface

class LocationPicker(val locationpicker: LocationPickerInterface ) {



    fun setLocation(): ArrayList<String> {
        var set = arrayListOf<String>()
        enumValues<Locations>().forEach { set.add(it.cityName) }
        return set
    }


}