package com.hva.bewear.main.provider

import android.content.Context
import com.hva.bewear.R
import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.presentation.main.provider.TextAdviceStringProvider

class AppTextAdviceStringProvider(
    private val context: Context
) : TextAdviceStringProvider {
    override fun getAdviceText(type: ClothingAdvice): String {
        return context.getString(
            when (type) {
                ClothingAdvice.WINTER_JACKET_LONG_PANTS -> R.string.winter_jacket_long_pants_base_string
                ClothingAdvice.SWEATER_LONG_PANTS -> R.string.sweater_long_pants_base_string
                ClothingAdvice.LONG_SHIRT_LONG_PANTS -> R.string.long_shirt_long_pants_base_string
                ClothingAdvice.SHORT_SHIRT_LONG_PANTS -> R.string.short_shirt_long_pants_base_string
                ClothingAdvice.SHORT_SHIRT_SHORT_PANTS -> R.string.short_shirt_short_pants_base_string
                else -> R.string.default_base_string
            }
        )
    }
}