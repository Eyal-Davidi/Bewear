package com.hva.hva_bewear.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hva.hva_bewear.R
import com.hva.hva_bewear.databinding.ActivityMainBinding
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val ui: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.weather.observe(this, ::handleWeather)
        viewModel.advice.observe(this, ::handleAdvice)
    }

    private fun handleWeather(weather: Weather) {
        ui.text.text = weather.toString()
    }

    private fun handleAdvice(advice: ClothingAdvice){
        ui.text.text = advice.textAdvice
    }
}
