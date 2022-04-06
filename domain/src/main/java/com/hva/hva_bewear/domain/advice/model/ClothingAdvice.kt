package com.hva.hva_bewear.domain.advice.model

enum class ClothingAdvice(var textAdvice: String, var wind: Boolean, var rain: Boolean, var highUVI: Boolean) {
    WINTER_JACKET_LONG_PANTS("Today will be a very cold%d day. You should wear a winter coat and long pants.", false, false, false),
    SWEATER_LONG_PANTS("Today will be a cold%d day. You should wear a sweater, jacket and long pants.", false, false, false),
    LONG_SHIRT_LONG_PANTS("Today will be a regular%d day. You should wear a long sleeved shirt, jacket and long pants.", false, false, false),
    SHORT_SHIRT_LONG_PANTS("Today will be a warm%d day. You should wear a short-sleeved shirt and long pants.", false, false, false),
    SHORT_SHIRT_SHORT_PANTS("Today will be a very warm%d day. You should wear a short-sleeved shirt and short pants.", false, false, false),
}

