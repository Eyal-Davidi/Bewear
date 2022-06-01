package com.hva.bewear.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.bewear.domain.advice.GetClothingAdvice
import com.hva.bewear.domain.advice.model.ClothingAdvice
import com.hva.bewear.domain.avatar_type.GetAvatarType
import com.hva.bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.bewear.domain.weather.GetWeather
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.domain.location.GetRecentLocations
import com.hva.bewear.domain.unit.GetUnit
import com.hva.bewear.domain.unit.SetUnit
import com.hva.bewear.domain.unit.model.MeasurementUnit
import com.hva.bewear.domain.location.LocationRepository
import com.hva.bewear.domain.location.model.LocationData
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
    private val getRecentLocations: GetRecentLocations,
    private val setUnit: SetUnit,
    private val getUnit: GetUnit,
    private val idProvider: AvatarIdProvider,
    private val stringProvider: TextAdviceStringProvider,
    private val idWeatherIconProvider: WeatherIconProvider,
    private val locationRepository: LocationRepository
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
        AdviceUIModel(avatar = idProvider.getAdviceLabel(ClothingAdvice.DEFAULT, avatarType.value))
    )
    val advice: StateFlow<AdviceUIModel> = _advice

    private val _locations: MutableStateFlow<List<LocationData>> = MutableStateFlow(emptyList())
    val locations: StateFlow<List<LocationData>> by lazy {
        fetchRecentLocations()
        _locations
    }

    private val _currentLocation: MutableStateFlow<LocationData> = MutableStateFlow(LocationData())
    val currentLocation: StateFlow<LocationData> = _currentLocation

    private val _isMetric: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isMetric: StateFlow<Boolean> = _isMetric

    private val _hourlyAdvice = MutableStateFlow(generateDefaultAdvice())
    val hourlyAdvice: StateFlow<List<AdviceUIModel>> = _hourlyAdvice

    private val _uiState = MutableStateFlow<UIStates>(UIStates.Normal)
    var uiState: StateFlow<UIStates> = _uiState

    fun refresh(
        location: LocationData? = null,
    ) = fetchAllData(location)

    private fun fetchAllData(location: LocationData?) {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.value = UIStates.Loading
            _locations.value = getRecentLocations()
            _typeOfAvatar.value = getAvatarType()
            _currentLocation.value = location ?:
            if(_locations.value.isEmpty()) LocationData("Amsterdam")
            else _locations.value.first()
            _isMetric.value = getUnit() == MeasurementUnit.METRIC
            val weather = fetchWeather(location ?: _currentLocation.value)
            _advice.value = fetchAdvice(weather = weather)
            _hourlyAdvice.value = List(
                size = AMOUNT_OF_HOURS_IN_HOURLY,
                init = { fetchAdvice(isHourly = true, index = it, weather = weather) }
            )
            _uiState.value = UIStates.Normal
        }
    }

    private suspend fun fetchWeather(location: LocationData): Weather {
        val weather = getWeather(location)
        _weather.value = weather.uiModel(idProvider = idWeatherIconProvider, getUnit = getUnit)
        return weather
    }

    private fun fetchAdvice(
        isHourly: Boolean? = null,
        index: Int? = null,
        weather: Weather,
    ): AdviceUIModel {
        return getClothingAdvice(isHourly = isHourly, index = index, weather = weather).uiModel(
            idProvider,
            stringProvider,
            avatarType.value,
        )
    }

    private fun fetchRecentLocations() {
        viewModelScope.launchOnIO {
            _locations.value = getRecentLocations()
        }
    }

    fun updateSettings(avatarType: AvatarType, isMetric: Boolean) {
        viewModelScope.launch {
            setTypeOfAvatar(avatarType)
            setUnit(if (isMetric) MeasurementUnit.METRIC else MeasurementUnit.IMPERIAL)
            refresh()
        }
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

    fun getLocation(text : String){
        viewModelScope.launchOnIO {
           _locations.value = locationRepository.getLocation(text)
            if (locations.value.isEmpty()){
                _locations.value = listOf(LocationData("No Locations found, please type more accurately"))
            }
        }
    }


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

    companion object {
        private const val AMOUNT_OF_HOURS_IN_HOURLY = 24
    }
}


