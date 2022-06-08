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
import com.hva.bewear.domain.location.model.Location
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
import kotlinx.coroutines.flow.*
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

    private val _currentLocation: MutableStateFlow<Location> = MutableStateFlow(DEFAULT_LOCATION)
    val currentLocation: StateFlow<Location> = _currentLocation

    private val _isMetric: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isMetric: StateFlow<Boolean> = _isMetric

    private val _hourlyAdvice = MutableStateFlow(generateDefaultAdvice())
    val hourlyAdvice: StateFlow<List<AdviceUIModel>> = _hourlyAdvice

    val searchText = MutableStateFlow("")
    private val _locations: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())
    val locations: StateFlow<List<Location>> = _locations

    private val _uiState = MutableStateFlow<UIStates>(UIStates.Normal)
    var uiState: StateFlow<UIStates> = _uiState

    init {
        viewModelScope.launchOnIO {
            searchText.debounce(750).collect {
                if(it.isNotBlank() && it.length > 2) {
                    Log.e("TAG", it)
                    getLocation(it)
                }
            }
        }
    }

    fun refresh(location: Location? = null) = fetchAllData(location)

    private fun fetchAllData(location: Location?) {
        viewModelScope.launchOnIO(fetchWeatherExceptionHandler) {
            _uiState.value = UIStates.Loading
            _locations.value = getRecentLocations()
            _typeOfAvatar.value = getAvatarType()
            _currentLocation.value = fetchSelectedLocation(location)
            _isMetric.value = getUnit() == MeasurementUnit.METRIC
            val weather = fetchWeather(location ?: _currentLocation.value)
            _advice.value = fetchAdvice(weather = weather)
            _hourlyAdvice.value = List(
                size = AMOUNT_OF_HOURS_IN_HOURLY,
                init = { fetchAdvice(isHourly = true, index = it, weather = weather) }
            )
            _locations.value = getRecentLocations()
            _uiState.value = UIStates.Normal
        }
    }

    private suspend fun fetchWeather(location: Location): Weather {
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

    private fun fetchSelectedLocation(location: Location?): Location {
        return location ?: if (_locations.value.isEmpty()) DEFAULT_LOCATION
        else _locations.value.first()
//            .takeUnless { it.isCurrent } ?: fetchLocation()
    }

    fun clearLocationSearch() {
        viewModelScope.launchOnIO {
            _locations.value = getRecentLocations()
        }
    }

    fun updateSettings(avatarType: AvatarType, isMetric: Boolean) {
        viewModelScope.launchOnIO() {
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

    fun getLocation(text: String) {
        viewModelScope.launchOnIO(getLocationExceptionHandler) {
            _locations.value = locationRepository.getLocation(text)
            if (locations.value.isEmpty() || locations.value.first().cityName.isBlank()) {
                _locations.value =
                    listOf(Location("", "No Locations found, please type more accurately"))
            }
        }
    }

    private val getLocationExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.tryEmit(
            when (throwable) {
                else ->
                    UIStates.Error("Something went wrong, error ${throwable.javaClass}")
            }
        )
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
        private val DEFAULT_LOCATION = Location(
            cityName = "Amsterdam",
            fullName = "Amsterdam, Netherlands",
            lat = 52.3676,
            lon = 4.9041,
        )
    }
}


