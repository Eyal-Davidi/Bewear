package com.hva.bewear.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import by.kirich1409.viewbindingdelegate.internal.findRootView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.hva.bewear.domain.avatar_type.model.AvatarType
import com.hva.bewear.domain.location.model.Location
import com.hva.bewear.main.theme.M2Mobi_HvATheme
import com.hva.bewear.main.theme.nunito
import com.hva.bewear.presentation.main.MainViewModel
import com.hva.bewear.presentation.main.model.UIStates
import com.hva.bewear.presentation.main.model.WeatherUIModel
import com.hva_bewear.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
        val settingIntentSnackbar = com.google.android.material.snackbar.Snackbar
            .make(
                findRootView(this),
                "Go to settings to allow location permission for Bewear!",5_000
            ).setAction("Settings") {
                startActivity(
                    Intent(
                        ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                Log.e("TAG", "onCreate: $isGranted")
                if (isGranted) fetchLocation()
                else settingIntentSnackbar.show()
            }
    }

    @Composable
    fun MainScreen(weather: WeatherUIModel) {
        val locations by viewModel.locations.collectAsState()
        val advice by viewModel.advice.collectAsState()
        val hourlyAdvice by viewModel.hourlyAdvice.collectAsState()
        val localFocusManager = LocalFocusManager.current

        BindStates {
            Loader(weather)
            Avatar(advice)

            Column(modifier = Modifier.pointerInput(Unit) {
                detectTapGestures { localFocusManager.clearFocus() }
            }) {
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
        val searchText by viewModel.searchText.collectAsState()
        var showLocationPermission by remember { mutableStateOf(false) }
        val currentLocation by viewModel.currentLocation.collectAsState()
        var hidePlaceholder by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val localFocusManager = LocalFocusManager.current
        val requester = FocusRequester()
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (expanded) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .clickable(interactionSource = interactionSource, indication = null) {
                            localFocusManager.clearFocus()
                        }
                )
            }
            Card(
                modifier = Modifier
                    .padding(5.dp, 5.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(TopCenter)
                    .clickable {
                        if (expanded) localFocusManager.clearFocus()
                        else requester.requestFocus()
                    },
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
                        val textFieldColors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            cursorColor = MaterialTheme.colors.primaryVariant
                        )
                        TextField(
                            value = searchText,
                            colors = textFieldColors,
                            onValueChange = {
                                viewModel.searchText.value = it
                                expanded = true
//                                if(text.length >= 3){
//                                    viewModel.getLocation(text)
//                                    expanded = true
//                                }
                            },
                            placeholder = {
                                if (!hidePlaceholder) Text(
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
                                    viewModel.getLocation(searchText)
                                    expanded = true
                                },
                            ),
                            modifier = Modifier
                                .align(CenterVertically)
                                .width(300.dp)
                                .requiredHeight(56.dp)
                                .focusRequester(requester)
                                .onFocusChanged {
                                    if (!it.isFocused) {
                                        viewModel.searchText.value = ""
                                        viewModel.clearLocationSearch()
                                    }
                                    hidePlaceholder = it.isFocused
                                    expanded = it.isFocused
                                }
                                .onKeyEvent {
                                    if ((it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_INSERT) || (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_SEARCH) || (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) {
                                        viewModel.getLocation(searchText)
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
                                .size(if (currentLocation.isCurrent) 34.dp else 43.dp)
                                .padding(end = if (currentLocation.isCurrent) 7.dp else 0.dp)
                                .align(CenterVertically)
                                .clickable {
                                    if (expanded) localFocusManager.clearFocus()
                                    else requester.requestFocus()
                                }
                        )
                    }
                }
            }
            if (expanded) {
                Card(
                    modifier = Modifier
                        .padding(start = 4.dp, top = 50.dp, end = 4.dp)
                        .width(400.dp)
                        .wrapContentHeight()
                        .verticalScroll(ScrollState(0)),
                    backgroundColor = MaterialTheme.colors.secondary,
                ) {
                    Column {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(
                                MaterialTheme.colors.secondary
                            )
                            .clickable {
                                localFocusManager.clearFocus()
                                if (checkLocationPermission()) showLocationPermission = true
                                else fetchLocation()
                            }) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
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
                                )
                            }
                        }
                        Divider()
                        locations.forEach { location ->
                            Column(
                                Modifier
                                    .clickable {
                                        localFocusManager.clearFocus()
                                        if (location != currentLocation && location.cityName.isNotBlank()) {
                                            viewModel.refresh(location)
                                        }
                                    }
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .background(
                                        MaterialTheme.colors.secondary
                                    ),
                            ) {
                                Text(
                                    text = location.toString(),
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .wrapContentWidth()
                                )
                            }
                            Divider()
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(R.drawable.powered_by_google_on_non_white),
                                contentDescription = "powered by Google",
                                modifier = Modifier
                                    .width(150.dp)
                                    .padding(start = 8.dp, top = 10.dp, bottom = 10.dp)
                            )
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
            is UIStates.NetworkError -> ErrorState(
                viewModel,
                errorState = uiState,
                showRefresh = true
            )
            is UIStates.CurrentLocationNetworkError -> ErrorState(
                viewModel,
                errorState = uiState,
                showRefresh = true,
                refreshFunction = {fetchLocation()}
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
        val cancellationTokenSource = CancellationTokenSource()

        if (checkLocationPermission()) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else viewModel.setLoading()

        fusedLocationProviderClient.getCurrentLocation(
            PRIORITY_LOW_POWER,
            cancellationTokenSource.token
        ).addOnSuccessListener {
            val isNetworkAvailable = isNetworkAvailable()
            if (it != null && isNetworkAvailable) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val address = if(addresses.isNotEmpty()) addresses[0] else Address(Locale.getDefault())
                viewModel.refresh(
                    location = Location(
                        cityName = address.locality ?: "Unknown City",
                        fullName = "${address.locality ?: "Unknown City"}, ${address.countryCode ?: "Unknown Country"}",
                        lat = it.latitude,
                        lon = it.longitude,
                        isCurrent = true
                    ),
                )
            } else if (!isNetworkAvailable) viewModel.setError(UIStates.CurrentLocationNetworkError("No connection!"))
            else viewModel.setNormal()
        }.addOnFailureListener { exception ->
            Log.d("Location", "Oops location failed with exception: $exception")
            viewModel.setNormal()
        }.addOnCanceledListener { viewModel.setNormal() }
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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
