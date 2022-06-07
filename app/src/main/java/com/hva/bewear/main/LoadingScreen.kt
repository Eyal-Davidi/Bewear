package com.hva.bewear.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R

@Composable
fun Loader(weather: WeatherUIModel) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            weather.backgroundId
        )
    )
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.33f,
        contentScale = ContentScale.FillBounds,
    )
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.day_night_loading)
            )
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
            )
            Text(text = "Loading", modifier = Modifier.padding(10.dp))
        }
    }
}
