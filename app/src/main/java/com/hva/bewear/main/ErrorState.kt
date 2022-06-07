package com.hva.bewear.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hva.bewear.presentation.main.MainViewModel
import com.hva.bewear.presentation.main.model.UIStates

@Composable
fun ErrorState(viewModel:MainViewModel,
    errorState: UIStates.ErrorInterface,
    showRefresh: Boolean = false
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
