package com.hva.hva_bewear.domain.avatar_type

import com.hva.hva_bewear.domain.avatar_type.data.AvatarTypeRepository
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType

class GetAvatarType(private val repository: AvatarTypeRepository) {
    suspend operator fun invoke(): AvatarType {
        return repository.getAvatarType()
    }
}