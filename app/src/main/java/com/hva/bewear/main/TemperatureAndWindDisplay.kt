package com.hva.bewear.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R

@Composable
fun TemperatureAndWindDisplay(weather: WeatherUIModel) {
    Column(Modifier.padding(start = 16.dp)) {
        Text(
            text = "Average:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Left,
            color = Color.Black,
        )
        Row()
        {
            Image(
                painter = painterResource(id = R.drawable.ic_thermometer),
                contentDescription = "Temperature image",
                modifier = Modifier
                    .size(38.dp)
            )
            Text(
                text = weather.temperatureDisplay,
                color = Color.Black,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }
        Text(
            text = weather.feelsLikeTemperatureDisplay,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(5.dp))
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                contentDescription = "Wind navigation image",
                modifier = Modifier
                    .size(24.dp)
                    .rotate(weather.windDegrees.toFloat())
            )
            Text(
                text = weather.windDisplay,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}