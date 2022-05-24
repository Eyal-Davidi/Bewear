package com.hva.bewear.presentation.main.provider

import androidx.annotation.DrawableRes
import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.model.AvatarType

interface AvatarIdProvider {
    @DrawableRes
    fun getAdviceLabel(type: ClothingAdvice, avatarType: AvatarType): Int
    @DrawableRes
    fun getExtraIcon(type: ClothingAdvice): List<Int>
}