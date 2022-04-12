package com.hva.hva_bewear.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import com.hva.hva_bewear.domain.weather.model.Weather
import com.hva.hva_bewear.main.theme.M2Mobi_HvATheme
import com.hva.hva_bewear.presentation.main.MainViewModel
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
        Column {
            AdviceDescription(advice)
            DisplayWeatherData(weather)
        }

    }

    @Composable
    fun DisplayWeatherData(weather:Weather?) {
        Text(
            text = weather.toString()
        )
    }

    @Composable
    fun AdviceDescription(advice: ClothingAdvice?){
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(10.dp),
            elevation = 10.dp,
        ) {
            Column {
                Text(
                    text = "Clothing Description",
                    modifier = Modifier
                        .scale(2f)
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                )
                AdviceText(advice = advice)
            }
        }
    }

    @Composable
    fun AdviceText(advice:ClothingAdvice?){
        if (advice != null) {
            Text(
                text = advice.textAdvice,
                modifier = Modifier
                    .padding(16.dp),
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
