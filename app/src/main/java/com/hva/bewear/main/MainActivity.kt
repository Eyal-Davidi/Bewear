package com.hva.bewear.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.main.theme.M2Mobi_HvATheme
import com.hva.bewear.main.theme.nunito
import com.hva.bewear.presentation.main.MainViewModel
import com.hva.bewear.presentation.main.model.AdviceUIModel
import com.hva.bewear.presentation.main.model.UIStates
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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
                Spacer(modifier = Modifier.height(65.dp))
                TitleDisplay()
                Spacer(modifier = Modifier.height(1.dp))
                Row {
                    TemperatureAndWindDisplay(weather)
                    Column(
                        horizontalAlignment = End,
                        modifier = Modifier
                            .padding(end = 26.dp)
                            .fillMaxWidth(),
                    ) {
                        WeatherIconAndExtraAdviceIconsDisplay(weather, advice)
//                        Spacer(Modifier.height(15.dp))
//                        ExtraAdviceIcons(advice)
                    }
                }
                BottomDisplay(advice, weather, hourlyAdvice)
            }
            TopBar(locations)

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
    fun TemperatureAndWindDisplay(weather: WeatherUIModel) {
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

            Spacer(Modifier.height(5.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
                    contentDescription = "Wind navigation image",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(weather.windDegrees.toFloat())
                )
                Text(
                    text = weather.windDisplay,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    @Composable
    fun WeatherIconAndExtraAdviceIconsDisplay(weather: WeatherUIModel, advice: AdviceUIModel) {
        val icons = advice.extraAdviceIcons
        Column(Modifier.padding(start = 0.dp, top = 0.dp) .width(100.dp))
        {
            Image(
                painter = painterResource(id = weather.iconId),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .scale(1.4f)
                    .wrapContentSize()
                    .align(End)

            )
            Spacer(Modifier.height(15.dp))
                Text(modifier = Modifier.align(End),
                    text = "Bring:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                )

                Row(Modifier.align(End)){
                    for (icon in icons) {
                        if(icons.size>1)
                        {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = "Extra advice icon",
                            modifier = Modifier
                                .offset(x = 20.dp)
                                .size(45.dp)
                        )
                        }
                        else{
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
//            Row {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_baseline_navigation_24),
//                    contentDescription = "Wind navigation image",
//                    modifier = Modifier
//                        .size(22.dp)
//                        .rotate(weather.windDegrees.toFloat())
//                )
//                Spacer(Modifier.width(5.dp))
//                Text(
//                    text = weather.windDisplay,
//                    color = Color.Black,
//                    fontSize = 17.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    textAlign = TextAlign.Center,
//                )
//            }
//        }
    }

    @Composable
    private fun TopBar(locations: List<Location>) {
        var expanded by remember { mutableStateOf(false) }
        var showPopup by remember { mutableStateOf(false) }
        var text by rememberSaveable { mutableStateOf("") }
        var showLocationPermission by remember { mutableStateOf(false) }
        val currentLocation by viewModel.currentLocation.collectAsState()
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (expanded) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null) {
                            expanded = false
                        }

                ) {

                }
            }
            Card(

                modifier = Modifier
                    .padding(5.dp, 5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(TopCenter),
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

                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                /*if(text.length >= 3){
                                    viewModel.getLocation(text)
                                    expanded = true
                                }*/
                            },
                            placeholder = {
                                Text(
                                    text = currentLocation.cityName,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Ascii,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    viewModel.getLocation(text)
                                    expanded = true
                                },
                            ),

                            modifier = Modifier
                                .align(CenterVertically)
                                .width(300.dp)
                                .requiredHeight(56.dp)
                                .onKeyEvent {
                                    if ((it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_INSERT) || (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_SEARCH) || (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) {
                                        viewModel.getLocation(text)
                                        expanded = true
                                        true
                                    } else {
                                        false
                                    }
                                },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            ),
                            singleLine = true,
                        )

                        Row {
                            if (currentLocation.isCurrent) Image(
                                painter = painterResource(R.drawable.ic_my_location),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(CenterVertically)
                            )
                            Image(
                                painter = if (expanded)
                                    painterResource(R.drawable.expand_less)
                                else
                                    painterResource(R.drawable.expand_more),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(CenterVertically)
                                    .clickable {
                                        expanded = !expanded

                                    }

                            )
                        }
                    }
                }
            }
            if (expanded) {
                Card(
                    modifier = Modifier

                        .padding(start = 4.dp, top = 45.dp)
                        .width(400.dp)
                        .height(220.dp)
                        .verticalScroll(ScrollState(0)),
                    backgroundColor = MaterialTheme.colors.primaryVariant,

                    ) {
                    Column {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colors.primaryVariant
                            )
                            .clickable {
                                expanded = false
                                if (checkLocationPermission()) showLocationPermission = true
                                else fetchLocation()
                            }) {
                            Row {
                                Text(
                                    text = "Current Location",
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .wrapContentWidth()


                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_my_location),
                                    contentDescription = null,
                                    alignment = Alignment.CenterEnd,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .offset(230.dp)

                                )
                            }
                        }
                        Divider()
                        locations.forEach { location ->
                            Column(
                                Modifier
                                    .clickable {
                                        if (location != currentLocation) {
                                            expanded = false
                                            viewModel.refresh(location)
                                        }
                                    }
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(
                                        MaterialTheme.colors.primaryVariant
                                    ),
                            ) {

                                Text(
                                    text = location.toString(),
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
                //here


            }




            if (showPopup) SettingsDialog(onShownChange = { showPopup = it })
            if (showLocationPermission) LocationPermissionDialog { showLocationPermission = it }
        }
    }

    @Composable
    fun LocationPermissionDialog(onShownChange: (Boolean) -> Unit) {
        CommonDialog(
            title = "Location Permission",
            onShownChange = onShownChange,
            onClickOkBtn = { fetchLocation() },
            okBtnText = "Ok",
        ) {
            Text(
                text = "To display the weather information of your current location Bewear needs access to your devices location." +
                        "\nDo you want to grant this permission?"
            )
        }
    }

    @Composable
    fun SettingsDialog(onShownChange: (Boolean) -> Unit) {
        var avatarType by remember { mutableStateOf(viewModel.avatarType.value.ordinal) }
        var isMetric by remember { mutableStateOf(viewModel.isMetric.value) }
        CommonDialog(
            title = "Settings",
            onShownChange = onShownChange,
            onClickOkBtn = { viewModel.updateSettings(AvatarType.values()[avatarType], isMetric) }
        ) {
            Column {
                Column {
                    Text(
                        "Avatar Type",
                        fontFamily = nunito,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .padding(bottom = 4.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            "Male",
                            fontFamily = nunito,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            "Both",
                            fontFamily = nunito,
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            "Female",
                            fontFamily = nunito,
                            fontWeight = FontWeight.Normal,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        RadioButton(selected = avatarType == 0, onClick = { avatarType = 0 })
                        RadioButton(selected = avatarType == 1, onClick = { avatarType = 1 })
                        RadioButton(selected = avatarType == 2, onClick = { avatarType = 2 })
                    }
                }

                Divider()

                Column {
                    Text(
                        "Unit of Measurement",
                        fontFamily = nunito,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(CenterHorizontally)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 9.dp)
                    ) {
                        Column {
                            Text(
                                "°C",
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .offset((-2).dp),
                                fontFamily = nunito,
                                fontWeight = FontWeight.Normal,
                            )
                            RadioButton(
                                selected = isMetric, onClick = { isMetric = true },
                                modifier = Modifier.align(CenterHorizontally)
                            )
                        }
                        Column {
                            Text(
                                "°F",
                                modifier = Modifier
                                    .align(CenterHorizontally)
                                    .offset((-1).dp),
                                fontFamily = nunito,
                                fontWeight = FontWeight.Normal,
                            )
                            RadioButton(
                                selected = !isMetric, onClick = { isMetric = false },
                                modifier = Modifier.align(CenterHorizontally),
                            )

                        }
                    }
                }
                Divider()
            }
        }
    }


    @Composable
    fun CommonDialog(
        title: String?,
        onShownChange: (Boolean) -> Unit,
        onClickOkBtn: () -> Unit,
        okBtnText: String = "Save",
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
                        Text(
                            text = title,
                            fontFamily = nunito,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                        Divider()
                    }
                }
            },
            text = content,
            dismissButton = {
                Button(onClick = { onShownChange(false) }) {
                    Text(
                        "Cancel",
                        fontFamily = nunito,
                        fontWeight = FontWeight.Normal,
                    )
                }
            },
            confirmButton = {
                Button(onClick = { onShownChange(false); onClickOkBtn() }) {
                    Text(
                        okBtnText,
                        fontFamily = nunito,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        )
    }

//    @Composable
//    fun ExtraAdviceIcons(advice: AdviceUIModel) {
//        val icons = advice.extraAdviceIcons
//        Column {
//            Text(
//                text = "Bring:",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.SemiBold,
//                textAlign = TextAlign.Center,
//                color = Color.Black,
//            )
//            Row {
//                for (icon in icons) {
//                    if(icons.size>1)
//                    {
//                    Image(
//                        painter = painterResource(icon),
//                        contentDescription = "Extra advice icon",
//                        modifier = Modifier
//                            .offset(x = -20.dp)
//                            .size(45.dp),
//                    )
//                    }
//                    else{
//                        Image(
//                            painter = painterResource(icon),
//                            contentDescription = "Extra advice icon",
//                            modifier = Modifier
//                                .offset(x = 0.dp)
//                                .size(45.dp),
//                        )
//                    }
//
//                    }
//            }
//        }
//    }

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
                        .align(TopCenter)
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
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .offset(y = 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color.LightGray)
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
                    border = BorderStroke(3.dp, Color.LightGray),
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

                    Column(horizontalAlignment = End) {
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

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                101
            )
        }

//        task.addOnCanceledListener {
//            val cancelled = "hi"
//        }
//        task.addOnFailureListener {
//            val exception = it
//        }
        task.addOnSuccessListener {
            if (it != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val address = addresses[0]
                viewModel.refresh(
                    location = Location(
                        cityName = address.locality,
                        state = address.adminArea,
                        country = address.countryCode,
                        lat = it.latitude,
                        lon = it.longitude,
                        isCurrent = true
                    ),
                )
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        M2Mobi_HvATheme {
            SettingsDialog(onShownChange = {})
        }
    }
}
