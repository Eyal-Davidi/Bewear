package com.hva.bewear.data.unit

import android.content.Context
import com.hva.bewear.data.BewearRoomDatabase
import com.hva.bewear.data.unit.network.UnitDao
import com.hva.bewear.data.unit.network.mapper.UnitMapper.toDomain
import com.hva.bewear.data.unit.network.mapper.UnitMapper.toResponse
import com.hva.bewear.domain.unit.data.UnitRepository
import com.hva.bewear.domain.unit.model.MeasurementUnit

class RemoteUnitRepository(context: Context) : UnitRepository {
    private var dao: UnitDao

    init {
        val roomDatabase = BewearRoomDatabase.getDatabase(context)
        dao = roomDatabase!!.unitDao()
    }

    override suspend fun getUnit(): MeasurementUnit = dao.getOne()?.toDomain() ?: MeasurementUnit.METRIC

    override suspend fun updateUnit(unit: MeasurementUnit) =
        dao.update(unit.toResponse())
}