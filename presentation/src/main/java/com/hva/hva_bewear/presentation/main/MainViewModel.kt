package com.hva.hva_bewear.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.advice.GetClothingAdvice
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
    private val getClothingAdvice: GetClothingAdvice,
    private val idProvider: AvatarIdProvider
    ) : ViewModel() {

    private val _weather = MutableStateFlow(WeatherUIModel())
    val weather: StateFlow<WeatherUIModel> by lazy {
        fetchWeather()
        _weather
    }

    private val _advice = MutableStateFlow(AdviceUIModel())
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
            _uiState.tryEmit(UIStates.Loading)
            getWeather().uiModel().let(_weather::tryEmit)
            _uiState.tryEmit(UIStates.Normal)
        }
    }

    fun fetchAdvice() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.tryEmit(UIStates.Loading)
            getClothingAdvice().uiModel(idProvider).let(_advice::tryEmit)
            _uiState.tryEmit(UIStates.Normal)
        }
    }
}
