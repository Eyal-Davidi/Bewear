package com.hva.bewear.di

import com.hva.bewear.domain.advice.GetClothingAdvice
import com.hva.bewear.domain.avatar_type.GetAvatarType
import com.hva.bewear.domain.avatar_type.SetTypeOfAvatar
import com.hva.bewear.domain.unit.GetUnit
import com.hva.bewear.domain.unit.SetUnit
import com.hva.bewear.domain.weather.GetWeather
import org.koin.dsl.factory
import org.koin.dsl.module

val domainModule = module {
    // Use cases
    factory { GetWeather(get()) }
    factory { GetClothingAdvice() }
    factory { GetAvatarType(get()) }
    factory { SetTypeOfAvatar(get()) }
    factory { GetUnit(get()) }
    factory { SetUnit(get()) }
}
