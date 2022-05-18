package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.provider.AvatarIdProvider
import com.hva.hva_bewear.presentation.main.provider.TextAdviceStringProvider

object AdviceUIMapper {

    fun ClothingAdvice.uiModel(idProvider: AvatarIdProvider, stringProvider: TextAdviceStringProvider, avatarType: AvatarType): AdviceUIModel {
        return AdviceUIModel(
            textAdvice = generateTextAdvice(this, stringProvider),
            avatar = idProvider.getAdviceLabel(type = this, avatarType),
        )
    }

    private fun generateTextAdvice(advice: ClothingAdvice, stringProvider: TextAdviceStringProvider) : String{
        var adviceString = stringProvider.getAdviceText(advice)
        val extraAdvice = stringProvider.getAdviceExtraAdvice(advice)
        val extraText = stringProvider.getAdviceExtraText(advice)

        adviceString = adviceString.replace("%d", extraText)
        adviceString = adviceString.plus(extraAdvice)

        return adviceString
    }
}