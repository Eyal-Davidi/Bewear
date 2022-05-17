package com.hva.hva_bewear.di

import com.hva.hva_bewear.domain.advice.GetClothingAdvice
import com.hva.hva_bewear.domain.avatar_type.GetAvatarType
import com.hva.hva_bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.hva_bewear.domain.text.GetText
import com.hva.hva_bewear.domain.weather.GetWeather
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetText(get()) }
    factory { GetWeather(get()) }
    factory { GetClothingAdvice() }
    factory { GetAvatarType(get()) }
    factory { SetTypeOfAvatar(get()) }
}
