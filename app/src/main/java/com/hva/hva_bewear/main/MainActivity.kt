package com.hva.hva_bewear.main

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize
import com.hva.hva_bewear.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        if(weather != null) TemperatureDisplay(weather!!)
        else GifImage(
            imageID = R.drawable.ic_action_loading,
        )
//        weather?.let {
//            TemperatureDisplay(weather = it)
//        }?: GifImage(imageID = R.drawable.ic_action_loading)
    }

    @Composable
    fun TemperatureDisplay(weather: WeatherUIModel) {
        Column(Modifier.padding(50.dp, 80.dp)) {
        Text(
            text = weather.temperatureDisplay,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }

        Column(Modifier.padding(30.dp, 120.dp)) {
            Text(
                text = weather.feelsLikeTemperatureDisplay,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }

        Column(Modifier.padding(22.dp, 88.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_action_thermometer),
                contentDescription = "Temperature image",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
        }
    }

    @Composable
    fun GifImage(
        modifier: Modifier = Modifier,
        imageID: Int
    ){
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }
            .build()
        Image(
            painter = rememberImagePainter(
                imageLoader = imageLoader,
                data = imageID,
                builder = {
                    size(OriginalSize)
                }
            ),
            contentDescription = null,
            modifier = modifier
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
