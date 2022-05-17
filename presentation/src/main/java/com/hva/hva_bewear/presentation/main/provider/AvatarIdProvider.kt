package com.hva.hva_bewear.presentation.main.provider

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType

interface AvatarIdProvider {
    fun getAdviceLabel(type: ClothingAdvice, avatarType: AvatarType): Int
}