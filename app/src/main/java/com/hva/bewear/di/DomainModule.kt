package com.hva.bewear.di

import com.hva.bewear.domain.advice.GetClothingAdvice
import com.hva.bewear.domain.avatar_type.GetAvatarType
import com.hva.bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.bewear.domain.weather.GetWeather
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetWeather(get()) }
    factory { GetClothingAdvice() }
    factory { GetAvatarType(get()) }
    factory { SetTypeOfAvatar(get()) }
}
