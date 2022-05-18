package com.hva.bewear.presentation.main.provider

import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.model.AvatarType

interface AvatarIdProvider {
    fun getAdviceLabel(type: ClothingAdvice, avatarType: AvatarType): Int
}