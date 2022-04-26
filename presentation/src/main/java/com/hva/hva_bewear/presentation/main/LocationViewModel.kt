package com.hva.hva_bewear.presentation.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hva.hva_bewear.domain.location.LocationPicker
import com.hva.hva_bewear.presentation.generic.launchOnIO

class LocationViewModel(locationpick: LocationPicker) : ViewModel() {

    private val _locations: MutableLiveData<List<String>> =
        MutableLiveData(locationpick.setOfLocations())
    val locations: LiveData<List<String>> by lazy {
        _locations
    }

    private val _currentLocation: MutableLiveData<String> =
        MutableLiveData(locations.value?.get(0) ?: "Amsterdam")
    val curentLocation: LiveData<String> by lazy {
        _currentLocation
    }

    fun getLocation(): String {
        return curentLocation.toString()
    }

    fun setLocation(s: String) {
        viewModelScope.launchOnIO {
            _currentLocation.postValue(s)
        }
    }
}