package com.hva.hva_bewear.presentation.main

import android.util.Log
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
import com.hva.hva_bewear.presentation.main.provider.AvatarIdProvider
import com.hva.hva_bewear.presentation.main.provider.TextAdviceStringProvider
import com.hva.hva_bewear.presentation.main.provider.WeatherIconProvider
import io.ktor.client.features.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.nio.channels.UnresolvedAddressException

class MainViewModel(
    private val getWeather: GetWeather,
    private val getClothingAdvice: GetClothingAdvice,
    private val idProvider: AvatarIdProvider,
    private val stringProvider: TextAdviceStringProvider,
    private val idWeatherIconProvider: WeatherIconProvider,
) : ViewModel() {

    private val _weather = MutableStateFlow(
        WeatherUIModel(
            iconId = idWeatherIconProvider.getWeatherIcon(""),
            backgroundId = idWeatherIconProvider.getWeatherBackground(""),
        )
    )
    val weather: StateFlow<WeatherUIModel> by lazy {
        fetchData()
        _weather
    }

    private val _advice = MutableStateFlow(
        AdviceUIModel(
            avatar = idProvider.getAdviceLabel(
                ClothingAdvice.DEFAULT
            )
        )
    )
    val advice: StateFlow<AdviceUIModel> by lazy {
        _advice
    }

    private val _hourlyAdvice = MutableStateFlow(
        List(
            size = AMOUNT_OF_HOURS_IN_HOURLY,
            init = {
                AdviceUIModel(
                    avatar = idProvider.getAdviceLabel(
                        ClothingAdvice.DEFAULT
                    )
                )
            }
        )
    )
    val hourlyAdvice: StateFlow<List<AdviceUIModel>> by lazy {
        _hourlyAdvice
    }

    private val _uiState = MutableStateFlow<UIStates>(UIStates.Normal)
    var uiState: StateFlow<UIStates> = _uiState


    private val fetchWeatherExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.tryEmit(
            when (throwable) {
                is UnresolvedAddressException ->
                    UIStates.NetworkError("No connection!")
                is ClientRequestException ->
                    UIStates.ClientRequestError(
                        "The limit of api calls is reached!\n " +
                                "Please contact the app owners"
                    )
                else ->
                    UIStates.Error("Something went wrong, error ${throwable.javaClass}")
            }
        )
        Log.e("AppERR", throwable.stackTraceToString())
    }

    fun refresh() {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.tryEmit(UIStates.Loading)
            getWeather().uiModel(idProvider = idWeatherIconProvider).let(_weather::tryEmit)
            getClothingAdvice().uiModel(idProvider, stringProvider).let(_advice::tryEmit)
            List(
                size = AMOUNT_OF_HOURS_IN_HOURLY,
                init = {
                    getClothingAdvice(isHourly = true, index = it).uiModel(idProvider, stringProvider)
                }
            ).let(_hourlyAdvice::tryEmit)
            _uiState.tryEmit(UIStates.Normal)
        }
    }

    companion object {
        private const val AMOUNT_OF_HOURS_IN_HOURLY = 24
    }
}
