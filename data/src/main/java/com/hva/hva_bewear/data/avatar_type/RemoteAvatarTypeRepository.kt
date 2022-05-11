package com.hva.hva_bewear.data.avatar_type

import android.content.Context
import com.hva.hva_bewear.data.avatar_type.network.mapper.AvatarTypeMapper.toDomain
import com.hva.hva_bewear.data.BewearRoomDatabase
import com.hva.hva_bewear.data.avatar_type.network.AvatarTypeDao
import com.hva.hva_bewear.data.avatar_type.network.mapper.AvatarTypeMapper.toResponse
import com.hva.hva_bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType

class RemoteAvatarTypeRepository(context: Context): AvatarTypeRepository {
    private var dao: AvatarTypeDao

    init {
        val roomDatabase = BewearRoomDatabase.getDatabase(context)
        dao = roomDatabase!!.avatarTypeDao()
    }

    override suspend fun getAvatarType(): AvatarType {
        return dao.getOne()!!.toDomain()
    }

    override suspend fun updateAvatarType(avatarType: AvatarType) {
        dao.update(avatarType.toResponse())
    }
}