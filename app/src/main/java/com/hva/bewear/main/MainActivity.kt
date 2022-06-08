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
            val weather by viewModel.weather.collectAsState()
            M2Mobi_HvATheme(weather) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(weather)
                }
            }
        }
    }

    @Composable
    fun MainScreen(weather: WeatherUIModel) {
        val locations by viewModel.locations.collectAsState()
        val advice by viewModel.advice.collectAsState()
        val hourlyAdvice by viewModel.hourlyAdvice.collectAsState()

        BindStates {
            Loader(weather)
            Avatar(advice)

            Column {
                Spacer(modifier = Modifier.height(50.dp))
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
                            indication = null
                        ) {
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
                backgroundColor = MaterialTheme.colors.primary,
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
                                .offset(x = 4.dp)
                                .align(CenterVertically)
                                .clickable { showPopup = true }
                        )
                        if (showPopup) SettingsDialog(onShownChange = { showPopup = it })

                        val textFieldColors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        )
                        TextField(
                            value = text,
                            colors = textFieldColors,
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
                                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
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
                            textStyle = MaterialTheme.typography.body1.copy(
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                            ),
                            singleLine = true,
                        )
                        Image(
                            painter = if (currentLocation.isCurrent)
                                painterResource(R.drawable.ic_my_location)
                            else if (expanded) painterResource(R.drawable.expand_less)
                            else painterResource(R.drawable.expand_more),
                            contentDescription = null,
                            modifier = Modifier
                                .size(if(currentLocation.isCurrent) 34.dp else 43.dp)
                                .padding(end = if(currentLocation.isCurrent) 7.dp else 0.dp)
                                .align(CenterVertically)
                                .clickable {
                                    expanded = !expanded
                                }
                        )
                    }
                }
            }
            if (expanded) {
                Card(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 50.dp, end = 4.dp,)
                        .width(400.dp)
                        .wrapContentHeight()
                        .verticalScroll(ScrollState(0)),
                    backgroundColor = MaterialTheme.colors.primary,
                ) {
                    Column {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colors.primary
                            )
                            .clickable {
                                expanded = false
                                if (checkLocationPermission()) showLocationPermission = true
                                else fetchLocation()
                            }) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
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
                                        MaterialTheme.colors.primary
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

    @Composable
    fun BindStates(Content: @Composable () -> Unit) {
        val state by viewModel.uiState.collectAsState()
        when (val uiState = state) {
            is UIStates.NetworkError -> ErrorState(viewModel,
                errorState = uiState,
                showRefresh = true
            )
            is UIStates.ErrorInterface -> ErrorState(viewModel, errorState = uiState)
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
                        fullName=  Location.SetFullName(address.locality, address.adminArea , address.countryCode ) ,
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
        val weather by viewModel.weather.collectAsState()
        M2Mobi_HvATheme(weather) {
            SettingsDialog(onShownChange = {})
        }
    }
}
