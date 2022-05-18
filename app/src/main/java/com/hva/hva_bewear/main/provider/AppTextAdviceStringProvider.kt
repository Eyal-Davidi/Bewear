package com.hva.hva_bewear.main.provider

import android.content.Context
import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice.*
import com.hva.hva_bewear.presentation.main.provider.TextAdviceStringProvider

class AppTextAdviceStringProvider(
    private val context: Context
) : TextAdviceStringProvider {

    override fun getAdviceText(type: ClothingAdvice): String {
        var string = context.getString(
            when (type) {
                WINTER_JACKET_LONG_PANTS -> R.string.winter_jacket_long_pants_base_string
                SWEATER_LONG_PANTS -> R.string.sweater_long_pants_base_string
                LONG_SHIRT_LONG_PANTS -> R.string.long_shirt_long_pants_base_string
                SHORT_SHIRT_LONG_PANTS -> R.string.short_shirt_long_pants_base_string
                SHORT_SHIRT_SHORT_PANTS -> R.string.short_shirt_short_pants_base_string
                else -> R.string.default_base_string
            })
        if (type == LONG_SHIRT_LONG_PANTS && !(type.wind || type.highUVI || type.rain)) string = string.replace("regular", "medium temperature")
        return string
    }

    override fun getAdviceExtraText(type: ClothingAdvice): String {
        return context.getString(
            when{
                type.wind && type.highUVI && type.rain -> R.string.extra_text_wind_highuvi_rain
                type.wind && type.highUVI -> R.string.extra_text_wind_highuvi
                type.wind && type.rain -> R.string.extra_text_wind_rain
                type.highUVI && type.rain -> R.string.extra_text_highuvi_rain
                type.wind -> R.string.extra_text_wind
                type.highUVI ->R.string.extra_text_highuvi
                type.rain -> R.string.extra_text_rain
                else -> R.string.extra_text_wind
            }
        )
    }
    override fun getAdviceExtraAdvice(type: ClothingAdvice): String {
        return context.getString(
            when{
                type.wind && type.highUVI && type.rain -> R.string.extra_advice_wind_highuvi_rain
                type.wind && type.highUVI -> R.string.extra_advice_wind_highuvi
                type.wind && type.rain -> R.string.extra_advice_wind_rain
                type.highUVI && type.rain -> R.string.extra_advice_highuvi_rain
                type.wind -> R.string.extra_advice_wind
                type.highUVI ->R.string.extra_advice_highuvi
                type.rain -> R.string.extra_advice_rain
                else -> R.string.extra_advice_wind
            }
        )
    }
}