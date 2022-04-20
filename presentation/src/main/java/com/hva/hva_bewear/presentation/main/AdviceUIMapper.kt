package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.provider.AvatarIdProvider
import com.hva.hva_bewear.presentation.main.provider.TextAdviceStringProvider

object AdviceUIMapper {

    fun ClothingAdvice.uiModel(idProvider: AvatarIdProvider, stringProvider: TextAdviceStringProvider): AdviceUIModel {
        return AdviceUIModel(
            textAdvice = generateTextAdvice(this, stringProvider.getAdviceText(this)),
            avatar = idProvider.getAdviceLabel(type = this),
        )
    }

    private fun generateTextAdvice(advice: ClothingAdvice, string: String) : String{
        var adviceString = string
        val extraAdvice: String
        val extraText: String

        when{
            advice.wind && advice.highUVI && advice.rain ->
            {
                extraText = ", windy, rainy and sunny"
                extraAdvice = " It will be rainy, windy and sunny! Take some sunscreen and an umbrella with you but don’t be careful with the wind."
            }
            advice.wind && advice.highUVI ->
            {
                extraText = ", windy and sunny"
                extraAdvice = " You should use some sunscreen and put on your hat and sunglasses, but be careful of the wind."
            }
            advice.wind && advice.rain ->
            {
                extraText = ", windy and rainy"
                extraAdvice = " Bring an umbrella but don't lose it to the wind!"
            }
            advice.highUVI && advice.rain ->
            {
                extraText = ", rainy and sunny"
                extraAdvice = " It will be both rainy and sunny, take some sunscreen and an umbrella with you."
            }
            advice.wind ->
            {
                extraText = " and windy"
                extraAdvice = ""
            }
            advice.highUVI ->
            {
                extraText = " and sunny"
                extraAdvice = " You should use some sunscreen and put on your hat and sunglasses."
            }
            advice.rain ->
            {
                extraText = " and rainy"
                extraAdvice = " Make sure to bring an umbrella."
            }
            else ->
            {
                extraText = ""
                extraAdvice = ""
            }
        }

        adviceString = adviceString.replace("%d", extraText)
        if (extraText != "") adviceString = adviceString.replace("regular", "medium temperature")
        adviceString = adviceString.plus(extraAdvice)

        return adviceString
    }
}