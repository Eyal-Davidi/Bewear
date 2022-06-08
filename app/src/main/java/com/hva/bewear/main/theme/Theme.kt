package com.hva.bewear.main.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R

private val SunnyColorPalette = lightColors(
    primary = PrimarySunny,
    primaryVariant = VariantSunny,
    secondary = SecondarySunny,
    background = PrimarySunny,
)

private val CloudyColorPalette = lightColors(
    primary = PrimaryCloudy,
    primaryVariant = VariantCloudy,
    secondary = SecondaryCloudy,
    background = PrimaryCloudy,
)

private val RainyColorPalette = lightColors(
    primary = PrimaryRainy,
    primaryVariant = VariantRainy,
    secondary = SecondaryRainy,
    background = PrimaryRainy,
)

private val SnowyColorPalette = lightColors(
    primary = PrimarySnowy,
    primaryVariant = VariantSnowy,
    secondary = SecondarySnowy,
    background = PrimarySnowy,
)

@Composable
fun M2Mobi_HvATheme(weather: WeatherUIModel, content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    val colors = when (weather.backgroundId) {
        R.raw.sunny_weather -> {
            systemUiController.setSystemBarsColor(PrimarySunny)
            SunnyColorPalette
        }
        R.raw.cloudy_weather_alt -> {
            systemUiController.setSystemBarsColor(PrimaryCloudy)
            CloudyColorPalette
        }
        R.raw.rainy_weather -> {
            systemUiController.setSystemBarsColor(PrimaryRainy)
            RainyColorPalette
        }
        R.raw.snow_weather -> {
            systemUiController.setSystemBarsColor(PrimarySnowy)
            SnowyColorPalette
        }
        else -> {
            systemUiController.setSystemBarsColor(PrimarySunny)
            SunnyColorPalette
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}