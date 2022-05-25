package com.hva.bewear.domain.unit

import com.hva.bewear.domain.unit.data.UnitRepository
import com.hva.bewear.domain.unit.model.MeasurementUnit

class GetUnit(private val repository: UnitRepository) {
    suspend operator fun invoke(): MeasurementUnit {
        return repository.getUnit()
    }
}