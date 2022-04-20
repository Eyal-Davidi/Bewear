package com.hva.hva_bewear.domain.advice.model

enum class ClothingAdvice(var wind: Boolean, var rain: Boolean, var highUVI: Boolean) {
    WINTER_JACKET_LONG_PANTS(false, false, false),
    SWEATER_LONG_PANTS(false, false, false),
    LONG_SHIRT_LONG_PANTS(false, false, false),
    SHORT_SHIRT_LONG_PANTS(false, false, false),
    SHORT_SHIRT_SHORT_PANTS(false, false, false),
    DEFAULT(false, false, false),
}

