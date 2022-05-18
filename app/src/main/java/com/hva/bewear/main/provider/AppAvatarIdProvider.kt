package com.hva.bewear.main.provider

import androidx.annotation.DrawableRes
import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.presentation.main.provider.AvatarIdProvider
import com.hva_bewear.R

class AppAvatarIdProvider : AvatarIdProvider {
    @DrawableRes
    override fun getAdviceLabel(type: ClothingAdvice, avatarType: AvatarType): Int {
        val avatarIsMale = when (avatarType) {
            AvatarType.MALE -> true
            AvatarType.FEMALE -> false
            else -> "010101".random() == '1'
        }

        return if(avatarIsMale) when (type) {
            ClothingAdvice.WINTER_JACKET_LONG_PANTS -> R.drawable.ava_m_winter_coat
            ClothingAdvice.SWEATER_LONG_PANTS -> R.drawable.ava_m_sweater
            ClothingAdvice.LONG_SHIRT_LONG_PANTS -> R.drawable.ava_m_long_sleeve
            ClothingAdvice.SHORT_SHIRT_LONG_PANTS -> R.drawable.ava_m_short_sleeve_long_pants
            ClothingAdvice.SHORT_SHIRT_SHORT_PANTS -> R.drawable.ava_m_short_sleeve_short_pants
            else -> R.drawable.default_placeholder
        } else when (type) {
            ClothingAdvice.WINTER_JACKET_LONG_PANTS -> R.drawable.ava_f_winter_coat
            ClothingAdvice.SWEATER_LONG_PANTS -> R.drawable.ava_f_sweater
            ClothingAdvice.LONG_SHIRT_LONG_PANTS -> R.drawable.ava_f_long_sleeve
            ClothingAdvice.SHORT_SHIRT_LONG_PANTS -> R.drawable.ava_f_short_sleeve_long_pants
            ClothingAdvice.SHORT_SHIRT_SHORT_PANTS -> R.drawable.ava_f_short_sleeve_short_pants
            else -> R.drawable.default_placeholder
        }
    }
}