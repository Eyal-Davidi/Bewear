package com.hva.appname.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hva.appname.domain.text.GetText

class MainViewModel(private val getText: GetText) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> by lazy {
        fetchText()
        _text
    }

    private fun fetchText() {
        getText().let(_text::postValue)
    }
}
