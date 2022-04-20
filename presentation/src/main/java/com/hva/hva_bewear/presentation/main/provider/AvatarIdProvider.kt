package com.hva.hva_bewear.presentation.main.provider

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice

interface AvatarIdProvider {
    fun getAdviceLabel(type: ClothingAdvice): Int
}