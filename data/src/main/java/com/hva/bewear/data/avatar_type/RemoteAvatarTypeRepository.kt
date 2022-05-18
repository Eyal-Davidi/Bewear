package com.hva.bewear.data.avatar_type

import android.content.Context
import com.hva.bewear.data.avatar_type.network.mapper.AvatarTypeMapper.toDomain
import com.hva.bewear.data.BewearRoomDatabase
import com.hva.bewear.data.avatar_type.network.AvatarTypeDao
import com.hva.bewear.data.avatar_type.network.mapper.AvatarTypeMapper.toResponse
import com.hva.bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.bewear.domain.avatar_type.model.AvatarType
import kotlinx.coroutines.delay

class RemoteAvatarTypeRepository(context: Context) : AvatarTypeRepository {
    private var dao: AvatarTypeDao

    init {
        val roomDatabase = BewearRoomDatabase.getDatabase(context)
        dao = roomDatabase!!.avatarTypeDao()
    }

    override suspend fun getAvatarType(): AvatarType = dao.getOne()?.toDomain() ?: AvatarType.MALE

    override suspend fun updateAvatarType(avatarType: AvatarType) =
        dao.update(avatarType.toResponse())
}