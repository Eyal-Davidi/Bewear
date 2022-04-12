package com.hva.hva_bewear.di

import com.hva.hva_bewear.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModels
    viewModel { MainViewModel(get(), get()) }
}
