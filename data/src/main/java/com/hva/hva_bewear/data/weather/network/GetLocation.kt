package com.hva.hva_bewear.data.weather.network

import com.hva.hva_bewear.domain.location.PassLocation



class GetLocation(private val pass : PassLocation) {
    suspend fun getLocation() : Locations {
       return Locations.CityName(pass.getLocation())
    }
}