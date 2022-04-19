package com.hva.hva_bewear.main

import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.presentation.main.AvatarIdProvider

class AppAvatarIdProvider() : AvatarIdProvider {
    override fun getAdviceLabel(type: ClothingAdvice): Int {
        return when (type) {
            ClothingAdvice.WINTER_JACKET_LONG_PANTS -> R.drawable.ic_action_cloudy
            ClothingAdvice.SWEATER_LONG_PANTS -> R.drawable.expand_less
            ClothingAdvice.LONG_SHIRT_LONG_PANTS -> R.drawable.expand_more
            ClothingAdvice.SHORT_SHIRT_LONG_PANTS -> R.drawable.logo
            ClothingAdvice.SHORT_SHIRT_SHORT_PANTS -> R.drawable.ic_action_thermometer
        }
    }
}