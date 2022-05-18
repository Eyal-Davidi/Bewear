package com.hva.bewear.presentation.main.provider

interface WeatherIconProvider {
    fun getWeatherIcon(type: String): Int
    fun getWeatherBackground(type: String): Int
}