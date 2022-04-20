package com.hva.hva_bewear.main

import androidx.annotation.DrawableRes
import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.presentation.main.AvatarIdProvider

class AppAvatarIdProvider : AvatarIdProvider {
    @DrawableRes
    override fun getAdviceLabel(type: ClothingAdvice): Int {
        return when (type) {
            ClothingAdvice.WINTER_JACKET_LONG_PANTS -> R.drawable.winter_coat
            ClothingAdvice.SWEATER_LONG_PANTS -> R.drawable.sweater
            ClothingAdvice.LONG_SHIRT_LONG_PANTS -> R.drawable.long_sleeve
            ClothingAdvice.SHORT_SHIRT_LONG_PANTS -> R.drawable.short_sleeve_long_pants
            ClothingAdvice.SHORT_SHIRT_SHORT_PANTS -> R.drawable.short_sleeve_short_pants
            else -> R.drawable.default_placeholder
        }
    }
}