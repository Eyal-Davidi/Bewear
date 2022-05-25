package com.hva.bewear.domain.unit.data

import com.hva.bewear.domain.unit.model.MeasurementUnit

interface UnitRepository {
    suspend fun getUnit(): MeasurementUnit
    suspend fun updateUnit(unit: MeasurementUnit)
}