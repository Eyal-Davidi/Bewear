package com.hva.bewear.presentation.main.provider

import com.hva.bewear.domain.advice.model.ClothingAdvice

interface TextAdviceStringProvider {
    fun getAdviceText(type: ClothingAdvice): String
}