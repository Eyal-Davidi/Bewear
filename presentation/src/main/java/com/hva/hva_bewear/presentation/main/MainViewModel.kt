package com.hva.hva_bewear.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.presentation.generic.launchOnIO
import com.hva.hva_bewear.presentation.main.AdviceUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.WeatherUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import com.hva.hva_bewear.presentation.main.provider.AvatarIdProvider
import com.hva.hva_bewear.presentation.main.provider.TextAdviceStringProvider
import kotlinx.coroutines.*

class MainViewModel(private val getWeather: GetWeather, private val getClothingAdvice: GetClothingAdvice, private val idProvider: AvatarIdProvider, private val stringProvider: TextAdviceStringProvider) : ViewModel() {

    private val _weather = MutableLiveData<WeatherUIModel>()
    val weather: LiveData<WeatherUIModel> by lazy {
        fetchWeather()
        _weather
    }

    private val _advice = MutableLiveData<AdviceUIModel>()
    val advice: LiveData<AdviceUIModel> by lazy {
        fetchAdvice()
        _advice
    }

    private val fetchWeatherExceptionHandler = CoroutineExceptionHandler { _, throwable ->  }

    fun fetchWeather() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            getWeather().uiModel().let(_weather::postValue)
        }
    }

    fun fetchAdvice(){
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            getClothingAdvice().uiModel(idProvider, stringProvider).let(_advice::postValue)
        }
    }
}
