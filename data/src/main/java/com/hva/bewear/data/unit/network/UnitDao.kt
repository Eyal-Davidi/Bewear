package com.hva.bewear.data.unit.network

import androidx.room.*
import com.hva.bewear.data.unit.network.response.UnitResponse

@Dao
interface UnitDao {
    @Query("SELECT * FROM UnitTable LIMIT 1")
    suspend fun getOne(): UnitResponse?

    @Insert
    suspend fun insert(unitResponse: UnitResponse)

    @Update
    suspend fun update(unitResponse: UnitResponse)
}