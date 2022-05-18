package com.hva.bewear.di

import com.hva.bewear.main.provider.AppWeatherIconProvider
import com.hva.bewear.main.provider.AppAvatarIdProvider
import com.hva.bewear.main.provider.AppTextAdviceStringProvider
import com.hva.bewear.presentation.main.provider.AvatarIdProvider
import com.hva.bewear.presentation.main.MainViewModel
import com.hva.bewear.presentation.main.provider.WeatherIconProvider
import com.hva.bewear.presentation.main.provider.TextAdviceStringProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModels
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory<AvatarIdProvider> { AppAvatarIdProvider() }
    factory<TextAdviceStringProvider> { AppTextAdviceStringProvider(get()) }
    factory<WeatherIconProvider> { AppWeatherIconProvider() }
}
