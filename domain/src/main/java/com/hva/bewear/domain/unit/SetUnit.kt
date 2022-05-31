package com.hva.bewear.domain.unit

import com.hva.bewear.domain.unit.data.UnitRepository
import com.hva.bewear.domain.unit.model.MeasurementUnit

class SetUnit(private val repository: UnitRepository) {
    suspend operator fun invoke(unit: MeasurementUnit) {
        return repository.updateUnit(unit)
    }
}