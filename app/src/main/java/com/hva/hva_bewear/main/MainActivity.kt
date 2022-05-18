package com.hva.hva_bewear.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hva.hva_bewear.R
import com.hva.hva_bewear.domain.avatar_type.model.AvatarType
import com.hva.hva_bewear.main.theme.M2Mobi_HvATheme
import com.hva.hva_bewear.presentation.main.MainViewModel
import com.hva.hva_bewear.presentation.main.model.AdviceUIModel
import com.hva.hva_bewear.presentation.main.model.UIStates
import com.hva.hva_bewear.presentation.main.model.WeatherUIModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.qualifier
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
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
        val locations by viewModel.locations.collectAsState()
        val weather by viewModel.weather.collectAsState()
        val advice by viewModel.advice.collectAsState()
        val hourlyAdvice by viewModel.hourlyAdvice.collectAsState()

        BindStates {
            Loader(weather)
            Avatar(advice)
            Column {
                TopBar(locations)
                TitleDisplay()
                Spacer(modifier = Modifier.height(1.dp))
                Row {
                    TemperatureDisplay(weather)
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(end = 26.dp)
                            .fillMaxWidth(),
                    ) {
                        WindDisplay(weather)
                    }
                }
                BottomDisplay(advice, weather, hourlyAdvice)
            }
        }
    }

    @Composable
    fun TitleDisplay() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
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
        var showPopup by remember { mutableStateOf(false) }
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
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = null,
                        modifier = Modifier
                            .size(27.dp)
                            .align(CenterVertically)
                            .clickable { showPopup = true }
                    )
                    if (showPopup) SettingsDialog(onShownChange = { showPopup = it })
                    Text(
                        locations[selectedIndex],
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(CenterVertically)
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
                                    viewModel.refresh(s)
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
    fun SettingsDialog(onShownChange: (Boolean) -> Unit) {
        var avatarType by remember { mutableStateOf(viewModel.avatarType.value.ordinal.toFloat()) }
        CommonDialog(
            title = "Avatar Settings",
            onShownChange = onShownChange,
            onClickOkBtn = {viewModel.updateTypeOfAvatar(AvatarType.values()[avatarType.toInt()])}
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Male")
                Text("Both")
                Text("Female")
            }
            Slider(
                value = avatarType,
                onValueChange = {avatarType = it},
                steps = 1,
                valueRange = 0f..2f,
                onValueChangeFinished = {avatarType = avatarType.roundToInt().toFloat()},
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    @Composable
    fun CommonDialog(
        title: String?,
        onShownChange: (Boolean) -> Unit,
        onClickOkBtn: () -> Unit,
        content: @Composable (() -> Unit)? = null
    ) {
        AlertDialog(
            onDismissRequest = { onShownChange(false) },
            title = title?.let {
                {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = title)
                        Divider(modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            },
            text = content,
            dismissButton = {
                Button(onClick = { onShownChange(false) }) {
                    Text("Cancel")
                }
            },
            confirmButton = {
                Button(onClick = { onShownChange(false); onClickOkBtn() }) {
                    Text("Save")
                }
            }, modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    @Composable
    fun BottomDisplay(advice: AdviceUIModel, weather: WeatherUIModel, hourlyAdvice: List<AdviceUIModel>){
        var descriptionOffsetY by remember { mutableStateOf(0f) }
        val maxDescriptionOffset = -110f
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
    fun AdviceDescription(advice: AdviceUIModel, modifier: Modifier = Modifier, dragAmount: Float = 1f) {
        val titleMinSize = 20
        val titleMaxSize = 30
        Card(
            shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp),
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .offset(y = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.Gray)
                        .align(CenterHorizontally)
                        .width(100.dp)
                        .height(6.dp),
                )
                Text(
                    text = "Clothing Description",
                    color = Color.Black,
                    modifier = Modifier
                        .align(CenterHorizontally)
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
            for (i in 0..23) {
                Card(
                    border = BorderStroke(3.dp, Color.Gray),
                    shape = RoundedCornerShape(topEnd = 5.dp, topStart = 5.dp),
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp),
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                ) {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Image(
                            painter = painterResource(hourlyAdvice[i].avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .offset(y = 30.dp)
                                .scale(1f),
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        val icon =
                            if (weather.hourlyIcons.isEmpty()) R.drawable.ic_action_cloudy
                            else weather.hourlyIcons[i]
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = "Weather Icon",
                            modifier = Modifier
                                .offset(x = 15.dp, y = (-10).dp)
                                .scale(0.75f)
                                .wrapContentSize(),
                        )
                    }

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = if (i == 0) "Now" else weather.hourlyWeather[i].date.hour.toString() + ":00",
                            color = Color.Black,
                            modifier = Modifier
                                .padding(start = 5.dp, top = 5.dp),
                            fontSize = 16.sp
                        )
                        Text(
                            text = weather.hourlyWeather[i].temperature.toInt()
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
    fun Avatar(advice: AdviceUIModel) {
        Image(
            painter = painterResource(advice.avatar),
            contentDescription = "Avatar",
            modifier = Modifier
                .offset(y = 100.dp)
                .scale(1f)
        )
    }

    @Composable
    fun BindStates(Content: @Composable () -> Unit) {
        val state by viewModel.uiState.collectAsState()
        when (val uiState = state) {
            is UIStates.NetworkError -> ErrorState(
                errorState = uiState,
                showRefresh = true
            )
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
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                weather.backgroundId
            )
        )
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            speed = 0.33f,
            contentScale = ContentScale.FillBounds,
        )
    }

    @Composable
    fun LoadingScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Center),
                horizontalAlignment = CenterHorizontally
            ) {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.RawRes(R.raw.day_night_loading)
                )
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
//                    speed = 0.33f,
                )
                Text(text = "Loading", modifier = Modifier.padding(10.dp))
            }
        }
    }

    @Composable
    fun ErrorState(
        errorState: UIStates.ErrorInterface,
        showRefresh: Boolean = false
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Center),
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = errorState.errorText,
                    modifier = Modifier.padding(10.dp)
                )
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


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M2Mobi_HvATheme {
        }
    }
}
