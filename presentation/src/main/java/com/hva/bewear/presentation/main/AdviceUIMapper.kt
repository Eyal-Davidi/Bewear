package com.hva.bewear.presentation.main

import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.presentation.main.model.AdviceUIModel
import com.hva.bewear.presentation.main.provider.AvatarIdProvider
import com.hva.bewear.presentation.main.provider.TextAdviceStringProvider

object AdviceUIMapper {

    fun ClothingAdvice.uiModel(idProvider: AvatarIdProvider, stringProvider: TextAdviceStringProvider, avatarType: AvatarType): AdviceUIModel {
        return AdviceUIModel(
            textAdvice = generateTextAdvice(this, stringProvider),
            avatar = idProvider.getAdviceLabel(type = this, avatarType),
        )
    }

    private fun generateTextAdvice(advice: ClothingAdvice, stringProvider: TextAdviceStringProvider) : String{
        var adviceString = stringProvider.getAdviceText(advice)
        val extraAdvice = stringProvider.getExtraAdvice(advice)
        val extraText = stringProvider.getExtraText(advice)

        adviceString = adviceString.replace("%d", extraText)
        adviceString = adviceString.plus(extraAdvice)

        return adviceString
    }
}