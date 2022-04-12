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

        return advice
    }


}
