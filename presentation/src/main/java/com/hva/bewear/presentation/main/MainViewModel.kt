package com.hva.bewear.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.bewear.domain.advice.GetClothingAdvice
import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.GetAvatarType
import com.hva.bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.bewear.domain.location.LocationPicker
import com.hva.bewear.domain.weather.GetWeather
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.domain.location.Coordinates
import com.hva.bewear.domain.weather.model.Weather
import com.hva.bewear.presentation.generic.launchOnIO
import com.hva.bewear.presentation.main.AdviceUIMapper.uiModel
import com.hva.bewear.presentation.main.WeatherUIMapper.uiModel
import com.hva.bewear.presentation.main.model.UIStates
import com.hva.bewear.presentation.main.model.AdviceUIModel
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva.bewear.presentation.main.provider.AvatarIdProvider
import com.hva.bewear.presentation.main.provider.TextAdviceStringProvider
import com.hva.bewear.presentation.main.provider.WeatherIconProvider
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
    val avatarType: StateFlow<AvatarType> = _typeOfAvatar

    private val _advice = MutableStateFlow(
        AdviceUIModel(
            avatar = idProvider.getAdviceLabel(
                ClothingAdvice.DEFAULT,
                avatarType.value,
            )
        )
    )
    val advice: StateFlow<AdviceUIModel> = _advice

    private val _locations: MutableStateFlow<List<String>> = MutableStateFlow(
        locationPick.setOfLocations()
    )
    val locations: StateFlow<List<String>> = _locations

    private val _currentLocation: MutableStateFlow<String> = MutableStateFlow(locations.value[1])
    val currentLocation: StateFlow<String> = _currentLocation

    private val _hourlyAdvice = MutableStateFlow(
        generateDefaultAdvice()
    )
    val hourlyAdvice: StateFlow<List<AdviceUIModel>> = _hourlyAdvice

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

    fun refresh(location: String = currentLocation.value, coordinates: Coordinates = Coordinates(0.0,0.0)) {
        fetchData(location, coordinates)

    }

    private fun fetchData(location: String, coordinates: Coordinates) {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.tryEmit(UIStates.Loading)
            getAvatarType().let { _typeOfAvatar.value = it }
            _currentLocation.value = (location)
            val weather = fetchWeather(location, coordinates)
            getClothingAdvice(weather = weather, coordinates = coordinates)
                .uiModel(idProvider, stringProvider, avatarType.value).let(_advice::tryEmit)
            List(
                size = AMOUNT_OF_HOURS_IN_HOURLY,
                init = {
                    getClothingAdvice(
                        isHourly = true,
                        index = it,
                        weather = weather,
                        coordinates = coordinates
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

    private suspend fun fetchWeather(location: String, coordinates: Coordinates):Weather  {
        val weather = getWeather(location, coordinates)
        _weather.value = weather.uiModel(idProvider = idWeatherIconProvider)
        return weather
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


