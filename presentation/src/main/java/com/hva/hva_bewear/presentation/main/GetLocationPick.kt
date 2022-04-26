package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.location.GetLocation

class GetLocationPick(val l :LocationViewModel) : GetLocation {
    override suspend fun getLocation(): String {
        return l.getLocation()
    }
}