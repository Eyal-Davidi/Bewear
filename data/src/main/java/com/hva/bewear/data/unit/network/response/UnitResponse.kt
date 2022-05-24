package com.hva.bewear.data.unit.network.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UnitTable")
class UnitResponse (
    @ColumnInfo(name = "isMetric")
    val unit: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
)