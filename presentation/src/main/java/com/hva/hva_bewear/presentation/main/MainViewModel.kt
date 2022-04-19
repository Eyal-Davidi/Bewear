package com.hva.hva_bewear.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.presentation.generic.launchOnIO
import com.hva.hva_bewear.presentation.main.AdviceUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.WeatherUIMapper.uiModel
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import kotlinx.coroutines.*

class MainViewModel(private val getWeather: GetWeather, private val getClothingAdvice: GetClothingAdvice) : ViewModel() {

    private val _weather = MutableLiveData<WeatherUIModel>()
    val weather: LiveData<WeatherUIModel> by lazy {
        fetchWeather()
        _weather
    }

    private val _advice = MutableLiveData<AdviceUIModel>()
    val advice: LiveData<AdviceUIModel> by lazy {
        fetchTextAdvice()
        _advice
    }

    private val fetchWeatherExceptionHandler = CoroutineExceptionHandler { _, throwable ->  }

    private fun fetchWeather() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            getWeather().uiModel().let(_weather::postValue)
        }
    }

    private fun fetchTextAdvice(){
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            getClothingAdvice().uiModel().let(_advice::postValue)
        }
    }
}
