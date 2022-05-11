package com.hva.hva_bewear.data.avatar_type.network

import androidx.room.*
import com.hva.hva_bewear.data.avatar_type.network.response.AvatarTypeResponse

@Dao
interface AvatarTypeDao {
    @Query("SELECT * FROM AvatarTypeTable LIMIT 1")
    suspend fun getOne(): AvatarTypeResponse?

    @Insert
    suspend fun insert(avatarType: AvatarTypeResponse)

    @Update
    suspend fun update(avatarType: AvatarTypeResponse)
}