package com.hva.hva_bewear.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.avatar_type.GetAvatarType
import com.hva.hva_bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.hva_bewear.domain.location.LocationPicker
import com.hva.hva_bewear.domain.weather.GetWeather
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType
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
    private val getAvatarType: GetAvatarType,
    private val setTypeOfAvatar: SetTypeOfAvatar,
    private val idProvider: AvatarIdProvider,
    private val stringProvider: TextAdviceStringProvider,
    private val idWeatherIconProvider: WeatherIconProvider,
    private val locationPick: LocationPicker,
) : ViewModel() {

    private val _weather = MutableStateFlow(
        WeatherUIModel(
            iconId = idWeatherIconProvider.getWeatherIcon(""),
            backgroundId = idWeatherIconProvider.getWeatherBackground(""),
        )
    )
    val weather: StateFlow<WeatherUIModel> by lazy {
        refresh()
        _weather
    }

    private val _typeOfAvatar = MutableStateFlow(AvatarType.MALE)
    val avatarType: StateFlow<AvatarType> by lazy {
        _typeOfAvatar
    }

    private val _advice = MutableStateFlow(
        AdviceUIModel(
            avatar = idProvider.getAdviceLabel(
                ClothingAdvice.DEFAULT,
                avatarType.value,
            )
        )
    )
    val advice: StateFlow<AdviceUIModel> by lazy {
        _advice
    }

    private val _locations: MutableStateFlow<List<String>> = MutableStateFlow(
        locationPick.setOfLocations()
    )
    val locations: StateFlow<List<String>> by lazy {
        _locations
    }

    private val _currentLocation: MutableStateFlow<String> = MutableStateFlow(locations.value[0])
    val currentLocation: StateFlow<String> by lazy {
        _currentLocation
    }

    private val _hourlyAdvice = MutableStateFlow(
        generateDefaultAdvice()
    )
    val hourlyAdvice: StateFlow<List<AdviceUIModel>> by lazy {
        _hourlyAdvice
    }

    private fun generateDefaultAdvice(): List<AdviceUIModel> {
        val list = arrayListOf<AdviceUIModel>()
        for (i in 1..AMOUNT_OF_HOURS_IN_HOURLY) {
            list.add(
                AdviceUIModel(
                    avatar = idProvider.getAdviceLabel(
                        ClothingAdvice.DEFAULT,
                        avatarType.value,
                    )
                )
            )
        }
        return list
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

    fun refresh(location: String = currentLocation.value) {
        fetchData(location)
    }

    private fun fetchData(location: String) {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.tryEmit(UIStates.Loading)

            getAvatarType().let(_typeOfAvatar::tryEmit)
            _currentLocation.tryEmit(location)
            val weatherResponse = getWeather(location)
            weatherResponse
                .uiModel(idProvider = idWeatherIconProvider)
                .let(_weather::tryEmit)
            getClothingAdvice.invoke(weather = weatherResponse)
                .uiModel(
                    idProvider,
                    stringProvider,
                    avatarType.value,
                ).let(_advice::tryEmit)
            List(
                size = AMOUNT_OF_HOURS_IN_HOURLY,
                init = {
                    getClothingAdvice.invoke(
                        isHourly = true,
                        index = it,
                        weather = weatherResponse
                    ).uiModel(
                        idProvider,
                        stringProvider,
                        avatarType.value,
                    )
                }
            ).let(_hourlyAdvice::tryEmit)
            _uiState.tryEmit(UIStates.Normal)
        }
    }

    fun updateTypeOfAvatar(avatarType: AvatarType) {
        viewModelScope.launch {
            setTypeOfAvatar(avatarType)
            refresh()
        }
    }

    companion object {
        private const val AMOUNT_OF_HOURS_IN_HOURLY = 24
    }
}


