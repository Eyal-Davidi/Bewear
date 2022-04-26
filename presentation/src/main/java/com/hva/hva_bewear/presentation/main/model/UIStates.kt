package com.hva.hva_bewear.presentation.main.model

sealed class UIStates {
    interface ErrorInterface{val errorText: String}
    data class NetworkError(override val errorText: String) : ErrorInterface, UIStates()
    data class ClientRequestError(override val errorText: String) : ErrorInterface, UIStates()
    data class Error(override val errorText: String) : ErrorInterface, UIStates()
    object Normal : UIStates()
    object Loading : UIStates()
}