package com.hva.bewear.presentation.main.model

sealed interface UIStates {
    interface ErrorInterface: UIStates {val errorText: String}
    data class NetworkError(override val errorText: String) : ErrorInterface, UIStates
    data class CurrentLocationNetworkError(override val errorText: String) : ErrorInterface, UIStates
    data class ClientRequestError(override val errorText: String) : ErrorInterface, UIStates
    data class Error(override val errorText: String) : ErrorInterface, UIStates
    object Normal : UIStates
    object Loading : UIStates
}