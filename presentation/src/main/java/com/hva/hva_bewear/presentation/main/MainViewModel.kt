package com.hva.hva_bewear.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.presentation.generic.launchOnIO
import com.hva.hva_bewear.presentation.main.AdviceUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.WeatherUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.model.UIStates
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.nio.channels.UnresolvedAddressException

class MainViewModel(
    private val getWeather: GetWeather,
    private val getClothingAdvice: GetClothingAdvice
    private val idProvider: AvatarIdProvider
    ) : ViewModel() {

    private val _weather = MutableStateFlow(WeatherUIModel())
    val weather: StateFlow<WeatherUIModel> by lazy {
        fetchWeather()
        _weather
    }

    private val _advice = MutableStateFlow(ClothingAdvice.DEFAULT)
    val advice: StateFlow<AdviceUIModel> by lazy {
        fetchAdvice()
        _advice
    }

    private val _uiState = MutableStateFlow<UIStates>(UIStates.Normal)
    var uiState: StateFlow<UIStates> = _uiState


    private val fetchWeatherExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.tryEmit(
            if (throwable is UnresolvedAddressException) {
                UIStates.NetworkError("No connection!")
            } else {
                UIStates.Error("Something went wrong, error ${throwable.javaClass}")
            }
        )
    }

    fun fetchWeather() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.value = UIStates.Loading
            getWeather().uiModel().let {
                _weather.value = it
            }
            _uiState.value = UIStates.Normal
        }
    }

    fun fetchAdvice() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.value = UIStates.Loading
            generateTextAdvice(getClothingAdvice()).let { _advice.value = it }
            _uiState.value = UIStates.Normal
            getClothingAdvice().uiModel(idProvider).let(_advice::postValue)
        }
    }

    private fun generateTextAdvice(advice: ClothingAdvice): ClothingAdvice {
        val extraAdvice: String
        val extraText: String
        when {
            advice.wind && advice.highUVI && advice.rain -> {
                extraText = ", windy, rainy and sunny"
                extraAdvice =
                    " It will be rainy, windy and sunny! Take some sunscreen and an umbrella with you but don’t be careful with the wind."
            }
            advice.wind && advice.highUVI -> {
                extraText = ", windy and sunny"
                extraAdvice =
                    " You should use some sunscreen and put on your hat and sunglasses, but be careful of the wind."
            }
            advice.wind && advice.rain -> {
                extraText = ", windy and rainy"
                extraAdvice = " Bring an umbrella but don't lose it to the wind!"
            }
            advice.highUVI && advice.rain -> {
                extraText = ", rainy and sunny"
                extraAdvice =
                    " It will be both rainy and sunny, take some sunscreen and an umbrella with you."
            }
            advice.wind -> {
                extraText = " and windy"
                extraAdvice = ""
            }
            advice.highUVI -> {
                extraText = " and sunny"
                extraAdvice = " You should use some sunscreen and put on your hat and sunglasses."
            }
            advice.rain -> {
                extraText = " and rainy"
                extraAdvice = " Make sure to bring an umbrella."
            }
            else -> {
                extraText = ""
                extraAdvice = ""
            }
        }

        advice.textAdvice = advice.textAdvice.replace("%d", extraText)
        if (extraText != "") advice.textAdvice =
            advice.textAdvice.replace("regular", "medium temperature")
        advice.textAdvice = advice.textAdvice.plus(extraAdvice)

        return advice
    }
}
