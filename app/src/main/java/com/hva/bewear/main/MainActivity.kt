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
import com.hva.bewear.domain.location.Coordinates
import com.hva.bewear.main.theme.M2Mobi_HvATheme
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

    private var showCurrentLocationIcon = false

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
                TopBar(locations)
                TitleDisplay()
                Spacer(modifier = Modifier.height(1.dp))
                Row {
                    TemperatureDisplay(weather)
                    Column(
                        horizontalAlignment = End,
                        modifier = Modifier
                            .padding(end = 26.dp)
                            .fillMaxWidth(),
                    ) {
                        WindDisplay(weather)
                        Spacer(Modifier.height(20.dp))
                        ExtraAdviceIcons(advice)
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
        var expanded by remember { mutableStateOf(true) }
        var showPopup by remember { mutableStateOf(false) }
        var text by rememberSaveable { mutableStateOf("") }
        val selectedLocation by remember { mutableStateOf(viewModel.currentLocation.value) }
        var showLocationPermission by remember { mutableStateOf(false) }
        val currentLocation by viewModel.currentLocation.collectAsState()

        Card(
            modifier = Modifier
                .padding(5.dp, 5.dp)
                .clip(RoundedCornerShape(10.dp))
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

                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = {
                            Text(
                                text = selectedLocation,
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
                            .width(350.dp)
                            .height(56.dp)
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
                    //}
                    Row {
                        if (showCurrentLocationIcon) Image(
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

                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(400.dp)
                        .height(220.dp)
                        .align(CenterHorizontally)
                        .background(
                            MaterialTheme.colors.primaryVariant
                        ),
                ) {

                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            showCurrentLocationIcon = true
                            if (checkLocationPermission()) showLocationPermission = true
                            else fetchLocation()

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                    Divider()
                    locations.forEachIndexed { index, location ->
                        DropdownMenuItem(
                            onClick = {
                                if (location != currentLocation) {
                                    expanded = false

                                    showCurrentLocationIcon = false
                                    viewModel.refresh(location)

                                }
                            },
                        ) {

                            Text(
                                text = location,
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
        CommonDialog(
            title = "Avatar Settings",
            onShownChange = onShownChange,
            onClickOkBtn = { viewModel.updateTypeOfAvatar(AvatarType.values()[avatarType]) }
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                ) {
                    Text("Male")
                    Text("Both")
                    Text("Female")
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
                        Text(text = title)
                        Divider(modifier = Modifier.padding(bottom = 16.dp))
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
                    Text(okBtnText)
                }
            }, modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    @Composable
    fun ExtraAdviceIcons(advice: AdviceUIModel) {
        val icons = advice.extraAdviceIcons
        Column {
            for (icon in icons) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "Extra advice icon",
                    modifier = Modifier
                        .offset(x = 10.dp)
                        .size(60.dp),
                )
            }
        }
    }

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

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
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
                val city: String = addresses[0].locality
                viewModel.refresh(
                    location = city,
                    coordinates = Coordinates(it.latitude, it.longitude)
                )
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
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
