package com.hva.bewear.main

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hva.bewear.presentation.main.model.AdviceUIModel
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R

@Composable
fun BottomDisplay(
    advice: AdviceUIModel,
    weather: WeatherUIModel,
    hourlyAdvice: List<AdviceUIModel>
) {
    var descriptionOffsetY by remember { mutableStateOf(0f) }
    val maxDescriptionOffset = -126f
    val dragMultiplier = 0.4f
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    descriptionOffsetY += (delta * dragMultiplier)
                    descriptionOffsetY = when {
                        descriptionOffsetY < maxDescriptionOffset -> maxDescriptionOffset
                        descriptionOffsetY > 0f -> 0f
                        else -> descriptionOffsetY
                    }
                }
            )
    ) {
        val height = 150
        val descriptionOffset = 29
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(height.dp)
        ) {
            AdviceDescription(
                advice = advice,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeightIn((height + descriptionOffset).dp, 300.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = (-descriptionOffset + descriptionOffsetY).dp),
                dragAmount = descriptionOffsetY / maxDescriptionOffset,
            )
            HourlyDisplay(
                weather, hourlyAdvice, modifier = Modifier
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun AdviceDescription(
    advice: AdviceUIModel,
    modifier: Modifier = Modifier,
    dragAmount: Float = 1f
) {
    val titleMinSize = 20
    val titleMaxSize = 30
    Card(
        shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .offset(y = 6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colors.primaryVariant)
                    .align(Alignment.CenterHorizontally)
                    .width(100.dp)
                    .height(6.dp),
            )
            Text(
                text = "Clothing Description",
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 5.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
                fontSize = (titleMinSize + ((titleMaxSize - titleMinSize) * dragAmount)).sp
            )
            AdviceText(advice = advice)
        }
    }
}

@Composable
fun HourlyDisplay(
    weather: WeatherUIModel,
    hourlyAdvice: List<AdviceUIModel>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .wrapContentWidth()
    ) {
        for (hour in 0..23) {
            Card(
                border = BorderStroke(3.dp, MaterialTheme.colors.primaryVariant),
                shape = RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp),
                backgroundColor = MaterialTheme.colors.primary,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(hourlyAdvice[hour].avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .offset(y = 30.dp)
                            .scale(0.95f),
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    val icon =
//                        if (weather.hourlyIcons.isEmpty()) R.drawable.blank_icon
//                        else weather.hourlyIcons[hour]
                        weather.hourlyIcons[hour]
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "Weather Icon",
                        modifier = Modifier
                            .offset(x = (-10).dp, y = (5).dp)
//                                .scale(0.65f)
                            .size(35.dp)
                            .wrapContentSize(),
                    )
                }

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = if (hour == 0) "Now" else weather.hourlyWeather[hour].date.hour.toString() + ":00",
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp),
                        fontSize = 16.sp
                    )
                    Text(
                        text = weather.hourlyWeather[hour].temperature.toInt()
                            .toString() + "°",
                        color = Color.Black,
                        modifier = Modifier
                            .padding(start = 5.dp, top = 1.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun AdviceText(advice: AdviceUIModel) {
    Text(
        text = advice.textAdvice,
        color = Color.Black,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 16.dp),
        textAlign = TextAlign.Start,
    )
}