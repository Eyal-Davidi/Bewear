package com.hva.appname.di

import com.hva.appname.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModels
    viewModel { MainViewModel(get()) }
}
