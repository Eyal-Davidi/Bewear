package com.hva.bewear.presentation.main.model

import androidx.annotation.DrawableRes


data class AdviceUIModel(
    val textAdvice: String = "",
    @DrawableRes val avatar: Int,
    @DrawableRes val extraAdviceIcons: List<Int> = emptyList(),
)
