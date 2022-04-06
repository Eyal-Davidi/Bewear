package com.hva.hva_bewear.domain.advice

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.Weather

class GetClothingAdvice(private val repository: WeatherRepository) {

    private lateinit var weather: Weather

    private lateinit var advice: ClothingAdvice

    suspend operator fun invoke(): ClothingAdvice {

        weather = repository.getWeather()
        val currentWeather = weather.daily[0]
        val feelsLike = currentWeather.feelsLike.day

        advice = when {
            feelsLike < 5 -> ClothingAdvice.WINTER_JACKET_LONG_PANTS
            feelsLike < 10 -> ClothingAdvice.SWEATER_LONG_PANTS
            feelsLike < 20 -> ClothingAdvice.LONG_SHIRT_LONG_PANTS
            feelsLike < 25 -> ClothingAdvice.SHORT_SHIRT_LONG_PANTS
            else -> ClothingAdvice.SHORT_SHIRT_SHORT_PANTS
        }

        //addition clothing options
        advice.wind = currentWeather.windSpeed > 6.95
        advice.rain = currentWeather.percentageOfPrecipitation > 0.3 && currentWeather.rain > 0.5
        advice.highUVI = currentWeather.uvIndex >= 5

        generateTextAdvice()

        return advice
    }

    private fun generateTextAdvice(){
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

        advice.textAdvice = advice.textAdvice.replace("%d", extraText)
        if (extraText != "") advice.textAdvice = advice.textAdvice.replace("regular", "medium temperature")
        advice.textAdvice = advice.textAdvice.plus(extraAdvice)
    }
}
