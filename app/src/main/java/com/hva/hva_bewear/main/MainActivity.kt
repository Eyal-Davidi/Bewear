package com.hva.hva_bewear.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import androidx.compose.ui.res.painterResource
import com.hva.hva_bewear.R
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hva.hva_bewear.main.theme.M2Mobi_HvATheme
import com.hva.hva_bewear.presentation.main.MainViewModel
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M2Mobi_HvATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val weather by viewModel.weather.observeAsState()
        val advice by viewModel.advice.observeAsState()

        if (weather != null && advice != null) Column {
            Row {
                TemperatureDisplay(weather!!)
                WindDisplay(weather!!)
            }
            AdviceDescription(advice)
        } else GifImage(
            imageID = R.drawable.ic_action_loading,
        )
    }

    @Composable
    fun TemperatureDisplay(weather: WeatherUIModel) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 160.dp)) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_action_thermometer),
                    contentDescription = "Temperature image",
                    modifier = Modifier
                        .size(45.dp)
                )
                Text(
                    text = weather.temperatureDisplay,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                text = weather.feelsLikeTemperatureDisplay,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )

        }
    }

    @Composable
    fun WindDisplay(weather: WeatherUIModel) {
        Column(Modifier.padding(80.dp, 0.dp))
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data (weather.iconUrl)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "text",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(128.dp)
            )

            Row {
                Image(
                    //i con provided by api
                    //painter = painterResource(id = R.drawable.ic_action_cloudy),
                    painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                    contentDescription = "Wind navigation image",
                    modifier = Modifier
                        .size(60.dp, 80.dp)
                    //.padding(1.dp, 20.dp)
                )

                Text(
                    text = weather.windDisplay,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    @Composable
    fun AdviceDescription(advice: ClothingAdvice?) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp),
            backgroundColor = Color.LightGray,
        ) {
            Column {
                Text(
                    text = "Clothing Description",
                    modifier = Modifier
                        .scale(2f)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp, 16.dp, 16.dp, 8.dp),
                )

                advice?.let {
                    AdviceText(advice = advice)
                }
            }
        }
    }


    @Composable
    fun AdviceText(advice: ClothingAdvice) {
        Text(
            text = advice.textAdvice,
            modifier = Modifier
                .padding(horizontal = 48.dp, vertical = 16.dp),
            textAlign = TextAlign.Center,
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M2Mobi_HvATheme {
            MainScreen()
        }
    }
}
