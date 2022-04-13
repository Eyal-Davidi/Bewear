package com.hva.hva_bewear.main

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hva.hva_bewear.domain.advice.model.ClothingAdvice
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.size.OriginalSize
import com.hva.hva_bewear.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.ImagePainter.State.Empty.painter
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

            TemperatureDisplay(weather!!)
            AdviceDescription(advice)
        }
        else GifImage(
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
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    fun GifImage(
        modifier: Modifier = Modifier,
        imageID: Int
    ) {
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
