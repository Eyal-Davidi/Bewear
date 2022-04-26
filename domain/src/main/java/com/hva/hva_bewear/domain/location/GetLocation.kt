package com.hva.hva_bewear.domain.location

interface GetLocation {
    suspend fun getLocation(): String
}