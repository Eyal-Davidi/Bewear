package com.hva.bewear.domain.location

interface LocationRepository {
    suspend fun getLocation(text : String): List<String>
}