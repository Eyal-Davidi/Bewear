package com.hva.bewear.data.unit.network.mapper

import com.hva.bewear.data.unit.network.response.UnitResponse
import com.hva.bewear.domain.unit.model.MeasurementUnit

object UnitMapper {
    fun UnitResponse.toDomain(): MeasurementUnit {
        return when (unit) {
            "imperial" -> MeasurementUnit.IMPERIAL
            "metric" -> MeasurementUnit.METRIC
            else -> MeasurementUnit.METRIC
        }
    }

    fun MeasurementUnit.toResponse(): UnitResponse {
        return UnitResponse(when(this) {
            MeasurementUnit.IMPERIAL -> "imperial"
            MeasurementUnit.METRIC -> "metric"
            else -> "metric"
        }, 1)
    }
}