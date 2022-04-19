package com.hva.hva_bewear.presentation.main

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel

object AdviceUIMapper {

    fun ClothingAdvice.uiModel(): AdviceUIModel {
        return AdviceUIModel(
            textAdvice = generateTextAdvice(this),
            avatar = 1
        )
    }

    private fun generateTextAdvice(advice: ClothingAdvice) : String{
        val extraAdvice: String
        val extraText: String
        val output = advice.textAdvice

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

        output.replace("%d", extraText)
        if (extraText != "") output.replace("regular", "medium temperature")
        output.plus(extraAdvice)

        return output
    }
}