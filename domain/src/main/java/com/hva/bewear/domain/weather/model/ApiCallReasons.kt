package com.hva.bewear.domain.weather.model

enum class ApiCallReasons(val msg: String, val makeCall: Boolean) {
    LOCATION_DID_NOT_EXIST(msg = "Location did not exist", true),
    FILE_WAS_NOT_JSON(msg = "File did not contain json", true),
    LOCATION_WAS_OUTDATED(msg = "Location cache contained outdated data", true),
    NORMAL(msg = "Everything is fine", false)
}