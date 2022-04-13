package com.hva.hva_bewear.presentation.main


import com.hva.hva_bewear.presentation.main.LocationPicker
import com.hva.hva_bewear.domain.weather.model.Locations


class LocationPicker() {
    private val locpick: LocationPicker = LocationPicker()
    operator fun invoke(): Locations {
        return locpick.invoke()
    }
    fun setLocation() : ArrayList<String>{
        return locpick.setLocation()
    }
    
}