package com.hva.hva_bewear.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.domain.weather.model.Weather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val getWeather: GetWeather) : ViewModel() {

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> by lazy {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(coroutineContext) {
                fetchText()
            }
        }
        _weather
    }

    private suspend fun fetchText() {
        getWeather().let(_weather::postValue)
    }
}
