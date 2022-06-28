package com.hva.bewear.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hva.bewear.presentation.main.MainViewModel
import com.hva.bewear.presentation.main.model.UIStates

@Composable
fun ErrorState(
    viewModel: MainViewModel,
    errorState: UIStates.ErrorInterface,
    showRefresh: Boolean = false,
    refreshFunction: () -> Unit = { viewModel.refresh(viewModel.currentLocation.value) }
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorState.errorText,
                modifier = Modifier.padding(10.dp)
            )
            if (showRefresh) {
                Row {
                    Button(
                        onClick = { viewModel.refresh() },
                        modifier = Modifier
                            .height(40.dp)
                            .width(100.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                    ) {
                        Text(text = "Back", color = Color.White)
                    }
                    Divider(Modifier.width(10.dp))
                    Button(
                        onClick = refreshFunction,
                        modifier = Modifier
                            .height(40.dp)
                            .width(100.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant)
                    ) {
                        Text(text = "Refresh", color = Color.White)
                    }
                }
            }
        }
    }
}
