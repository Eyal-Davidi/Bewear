package com.hva.hva_bewear.domain.location

import com.hva.hva_bewear.domain.weather.model.Locations
import com.hva.hva_bewear.domain.location.GetLocation

class PassLocation(val getlocation: GetLocation) {

    suspend fun getLocation() : Locations {
        return Locations.CityName(getlocation.getLocation())
    }
}