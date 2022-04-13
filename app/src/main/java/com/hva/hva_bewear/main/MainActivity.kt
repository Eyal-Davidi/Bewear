package com.hva.hva_bewear.main

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    @Composable
    private fun TopBar() {
        var expanded by remember { mutableStateOf(false) }
        val locations = listOf("Amsterdam", "Rotterdam", "Hoorn")
        var selectedIndex by remember { mutableStateOf(0) }
        Box(
            modifier = Modifier
                .padding(5.dp,5.dp)
                .fillMaxSize()
                .wrapContentSize(Alignment.TopCenter)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Color.Gray
                )
        ) {
            Text(
                locations[selectedIndex],
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.Gray
                    )
            ) {
                locations.forEachIndexed { index, s ->
                    DropdownMenuItem(
                        onClick = {
                            if (index != selectedIndex) {
                                selectedIndex = index
                                expanded = false
                            }
                        },
                        modifier = Modifier.border(width = 1.dp, color = Color.Black)
                    ) {
                        Text(
                            text = s,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )

            Image(

                painter =
                if(expanded){
                    painterResource(R.drawable.expand_less)
                } else{
                    painterResource(R.drawable.expand_more)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp,20.dp)
                    .align(Alignment.TopEnd)
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
