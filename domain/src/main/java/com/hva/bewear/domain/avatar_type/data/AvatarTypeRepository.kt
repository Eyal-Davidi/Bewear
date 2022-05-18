package com.hva.bewear.domain.avatar_type.data

import com.hva.bewear.domain.avatar_type.model.AvatarType

interface AvatarTypeRepository {
    suspend fun getAvatarType(): AvatarType
    suspend fun updateAvatarType(avatarType: AvatarType)
}