package com.hva.hva_bewear.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.weather.model.DailyWeather
import com.hva.hva_bewear.main.theme.M2Mobi_HvATheme
import com.hva.hva_bewear.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.round

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
        if (weather != null)
            TempratureDisplay(weather!!.daily[0])
    }

    @Composable
    fun TempratureDisplay(dailyWeather: DailyWeather) {
        Column(Modifier.padding(50.dp, 80.dp)) {
        Text(
            //text = " $temprature-1)-1 °C",
            text = "${round(dailyWeather.temperature.day).toInt()}°C",
            //color = Color.BLACK,
            //modifier = Modifier.padding(40.dp, 60.dp),
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }

        Column(Modifier.padding(22.dp, 75.dp)) {
            Text(
                text = " \n\n Feels like: \n ${round(dailyWeather.feelsLike.day).toInt()}°C",
                //color = Color.BLACK,
                //modifier = Modifier.padding(40.dp, 60.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
//            Image(
//                painter = painterResource(id = R.drawable.ic_thermometer),
//                contentDescription = "Temprature image",
//                modifier = Modifier
//                    .width(10.dp)
//                    .height(10.dp)
//            )
        }

        Column(Modifier.padding(22.dp, 88.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_action_thermometer),
                contentDescription = "Temprature image",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M2Mobi_HvATheme {
            MainScreen()
        }
    }
}
