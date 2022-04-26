package com.hva.hva_bewear.domain.location


class PassLocation(private val getlocation: GetLocation) {

    suspend fun getLocation(): String {
        return getlocation.getLocation()
    }
}