package com.hva.hva_bewear.domain.weather.model

enum class Locations(val cityName: String, val lat : Double,val lon : Double) {
    AMSTERDAM("Amsterdam",52.3676,4.9041),
    ROTTERDAM("Rotterdam",51.924419,4.477733),
    HOORN("Hoorn",52.641460,5.056810),
    AVENHOORN("Avenhorn",52.616901,4.947843),
    NORGE("norge",79.349848,21.066975)
    ;



    companion object {

        fun CityName(city: String): Locations {
            enumValues<Locations>().forEach { if(it.cityName==city ) return it }
            return AMSTERDAM
        }
    }

}