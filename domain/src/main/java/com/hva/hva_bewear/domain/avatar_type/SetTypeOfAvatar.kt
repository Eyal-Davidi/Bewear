package com.hva.hva_bewear.domain.avatar_type

import com.hva.hva_bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType

class SetTypeOfAvatar(private val repository: AvatarTypeRepository) {
    suspend operator fun invoke(avatarType: AvatarType) {
        return repository.updateAvatarType(avatarType)
    }
}