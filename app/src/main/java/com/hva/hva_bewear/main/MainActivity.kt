package com.hva.hva_bewear.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hva.hva_bewear.R
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hva.hva_bewear.main.theme.M2Mobi_HvATheme
import com.hva.hva_bewear.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.hva.hva_bewear.presentation.main.LocationViewModel
import com.hva.hva_bewear.presentation.main.model.*
import kotlin.time.Duration.Companion.hours

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    private val locationViewModel: LocationViewModel by viewModel()
    private var selectedIndex = 0

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
        val locations by locationViewModel.locations.observeAsState()
        val weather by viewModel.weather.collectAsState()
        val advice by viewModel.advice.collectAsState()
        val hourlyAdvice by viewModel.hourlyAdvice.collectAsState()

        BindStates {
            Loader(weather)
            Avatar(advice)
            Column {
                locations?.let { TopBar(it) }
                TitleDisplay()
                Spacer(modifier = Modifier.height(1.dp))
                Row {
                    TemperatureDisplay(weather)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(end = 26.dp)
                            .fillMaxWidth(),
                    ){
                        WindDisplay(weather)
                    }

                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxHeight(),
                ) {
                    AdviceDescription(advice)
                }
            }
        }
    }

    @Composable
    fun TitleDisplay() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Today's Advice",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
            )

        }
    }

    @Composable
    fun TemperatureDisplay(weather: WeatherUIModel) {
        Column(Modifier.padding(start = 16.dp)) {
            Text(
                text = "Average:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,
                color = Color.Black,
            )
//            Row(Modifier.padding(top = 45.dp))
            Row()
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_action_thermometer),
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
        }
    }

    @Composable
    fun WindDisplay(weather: WeatherUIModel) {
        Column(Modifier.padding(start = 0.dp, top = 30.dp))
        {
            Image(
                painter = painterResource(id = weather.iconId),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .scale(1.5f)
                    .wrapContentSize(),
            )
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                    contentDescription = "Wind navigation image",
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(weather.windDegrees.toFloat())
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = weather.windDisplay,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    @Composable
    private fun TopBar(locations: List<String>) {
        var expanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .padding(5.dp, 5.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = { expanded = true })
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        locations[selectedIndex],
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Image(
                        painter = if (expanded)
                            painterResource(R.drawable.expand_less)
                        else
                            painterResource(R.drawable.expand_more),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(382.dp)
                        .height(220.dp)
                        .background(
                            MaterialTheme.colors.primaryVariant
                        ),
                ) {
                    Divider()
                    locations.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            onClick = {
                                if (index != selectedIndex) {
                                    selectedIndex = index
                                    expanded = false
                                    locationViewModel.setLocation(s)
                                    viewModel.refresh()
                                }
                            }
                        ) {
                            Text(
                                text = s,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .wrapContentWidth()
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }

    @Composable
    fun AdviceDescription(advice: AdviceUIModel) {
        Card(
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Column {
                Text(
                    text = "Clothing Description",
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp, 16.dp, 16.dp, 0.dp),
                    fontSize = 30.sp
                )
                AdviceText(advice = advice)
            }
        }
    }

    @Composable
    fun AdviceText(advice: AdviceUIModel) {
        Text(
            text = advice.textAdvice,
            color = Color.Black,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 16.dp),
            textAlign = TextAlign.Start,
        )
    }

    @Composable
    fun Avatar(advice: AdviceUIModel) {
        Image(
            painter = painterResource(advice.avatar),
            contentDescription = "Avatar",
            modifier = Modifier
                .offset(y = 100.dp)
                .scale(1f),
        )
    }

    @Composable
    fun BindStates(Content: @Composable () -> Unit) {
        val state by viewModel.uiState.collectAsState()
        when (val uiState = state) {
            is UIStates.NetworkError -> ErrorState(errorState = uiState, showRefresh = true)
            is UIStates.ErrorInterface -> ErrorState(errorState = uiState)
            UIStates.Loading -> LoadingScreen()
            UIStates.Normal -> Content()
            else -> {
                Toast.makeText(
                    applicationContext,
                    "Something went really really wrong...\n" +
                            "(Unknown application state)",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @Composable
    fun Loader(weather: WeatherUIModel) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(weather.backgroundId))
        LottieAnimation(composition)

        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            speed = 0.33f,
        )


    }

    @Composable
    fun LoadingScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                GifImage(imageID = R.drawable.day_night, modifier = Modifier.size(100.dp))
                Text(text = "Loading", modifier = Modifier.padding(10.dp))
            }
        }
    }

    @Composable
    fun ErrorState(errorState: UIStates.ErrorInterface, showRefresh: Boolean = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = errorState.errorText, modifier = Modifier.padding(10.dp))
                if (showRefresh) {
                    Button(
                        onClick = { viewModel.refresh() },
                        modifier = Modifier
                            .height(40.dp)
                            .width(100.dp),
                    ) {
                        Text(text = "Refresh")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M2Mobi_HvATheme {
            locationViewModel.locations.value?.let { TopBar(locations = it) }
        }
    }
}
