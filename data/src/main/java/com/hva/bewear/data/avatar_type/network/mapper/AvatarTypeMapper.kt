package com.hva.bewear.data.avatar_type.network.mapper

import com.hva.bewear.data.avatar_type.network.response.AvatarTypeResponse
import com.hva.bewear.domain.avatar_type.model.AvatarType

object AvatarTypeMapper {
    fun AvatarTypeResponse.toDomain(): AvatarType {
        return when (avatarType) {
            "m" -> AvatarType.MALE
            "f" -> AvatarType.FEMALE
            else -> AvatarType.BOTH
        }
    }

    fun AvatarType.toResponse(): AvatarTypeResponse {
        return AvatarTypeResponse(when(this) {
            AvatarType.MALE -> "m"
            AvatarType.FEMALE -> "f"
            else -> "b"
        }, 1)
    }
}