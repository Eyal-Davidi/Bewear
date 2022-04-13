package com.hva.hva_bewear.presentation.main


import com.hva.hva_bewear.domain.weather.LocationPicker


class LocationPicker() {
    private val locationpick: LocationPicker =com.hva.hva_bewear.domain.weather.LocationPicker()


    fun setOfLocations() : ArrayList<String>{
        return locationpick.setOfLocations()
    }
    fun setLocation(location: String){
        locationpick.setLocation(location)
    }
}