package com.hva.bewear.data.avatar_type.network.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AvatarTypeTable")
class AvatarTypeResponse (
    @ColumnInfo(name = "ava_type")
    val avatarType: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,
)