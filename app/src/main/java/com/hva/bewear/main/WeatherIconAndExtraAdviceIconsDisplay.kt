package com.hva.bewear.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hva.bewear.presentation.main.model.AdviceUIModel
import com.hva.bewear.presentation.main.model.WeatherUIModel

@Composable
fun WeatherIconAndExtraAdviceIconsDisplay(weather: WeatherUIModel, advice: AdviceUIModel) {
    val icons = advice.extraAdviceIcons
    Column(
        Modifier
            .padding(start = 0.dp, top = 0.dp)
            .width(100.dp))
    {
        Image(
            painter = painterResource(id = weather.iconId),
            contentDescription = "Weather Icon",
            modifier = Modifier
                .scale(1.4f)
                .wrapContentSize()
                .align(Alignment.End)

        )
        Spacer(Modifier.height(15.dp))
        if(icons.isNotEmpty())
            Text(
                modifier = Modifier.align(Alignment.End),
                text = "Bring:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,
                color = Color.Black,
            )
        Spacer(Modifier.height(5.dp))
        Row(Modifier.align(Alignment.End)) {
            for (icon in icons) {
                if (icons.size > 1) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "Extra advice icon",
                        modifier = Modifier
                            .offset(x = 20.dp)
                            .size(45.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = "Extra advice icon",
                        modifier = Modifier
                            .offset(x = 0.dp)
                            .size(45.dp),
                    )
                }
            }
        }
    }
}
