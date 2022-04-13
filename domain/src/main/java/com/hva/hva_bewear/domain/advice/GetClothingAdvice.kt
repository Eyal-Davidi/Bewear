package com.hva.hva_bewear.domain.advice

import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.data.WeatherRepository
import com.hva.hva_bewear.domain.weather.model.Weather

class GetClothingAdvice(private val repository: WeatherRepository) {

    suspend operator fun invoke(): ClothingAdvice {
        val weather = repository.getWeather()

        val currentWeather = weather.daily[0]
        val feelsLike = currentWeather.feelsLike.day

        val advice = when {
            feelsLike < WINTER_JACKET_THRESHOLD -> ClothingAdvice.WINTER_JACKET_LONG_PANTS
            feelsLike < SWEATER_THRESHOLD -> ClothingAdvice.SWEATER_LONG_PANTS
            feelsLike < LONG_SHIRT_THRESHOLD -> ClothingAdvice.LONG_SHIRT_LONG_PANTS
            feelsLike < SHORT_SHIRT_THRESHOLD -> ClothingAdvice.SHORT_SHIRT_LONG_PANTS
            else -> ClothingAdvice.SHORT_SHIRT_SHORT_PANTS
        }

        //additional clothing options
        advice.wind = currentWeather.windSpeed > WIND_SPEED_THRESHOLD
        advice.rain = currentWeather.percentageOfPrecipitation > CHANCE_OF_RAIN_THRESHOLD && currentWeather.rain > TOTAL_RAIN_THRESHOLD
        advice.highUVI = currentWeather.uvIndex >= UV_INDEX_THRESHOLD

        return advice
    }

    companion object{
        private const val WIND_SPEED_THRESHOLD = 6.95
        private const val CHANCE_OF_RAIN_THRESHOLD = 0.3
        private const val TOTAL_RAIN_THRESHOLD = 0.5
        private const val UV_INDEX_THRESHOLD = 5

        private const val WINTER_JACKET_THRESHOLD = 5
        private const val SWEATER_THRESHOLD = 10
        private const val LONG_SHIRT_THRESHOLD = 20
        private const val SHORT_SHIRT_THRESHOLD = 25


    }


}
