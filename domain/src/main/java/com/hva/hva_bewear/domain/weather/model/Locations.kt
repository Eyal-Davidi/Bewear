package com.hva.hva_bewear.domain.weather.model

enum class Locations(val cityName: String, val lat : Double,val lon : Double) {
    AMSTERDAM("Amsterdam",52.3676,4.9041),
    ARNHEM("Arnhem", 51.9851034, 5.8987296),
    ASSEN("Assen", 52.993668, 6.558259),
    //AVENHOORN("Avenhorn",52.616901,4.947843),
    BEIJING("Beijing", 39.904211, 116.407395),
    BRASILIA("Brasilia", -15.7942287, -47.8821658),
    BREDA("Breda", 51.5719149, 4.768323),
    DEN_BOSCH("Den Bosch", 51.6978162, 5.3036748),
    DEN_HAAG("Den Haag", 52.078663, 4.288788),
    DEN_HELDER("Den Helder", 52.9562808, 4.7607972),
    EINDHOVEN("Eindhoven", 51.434619, 5.486011),
    ENSCHEDE("Enschede",  52.2215372, 6.8936619),
    GRONINGEN("Groningen", 53.2190652, 6.5680077),
    HAARLEM("Haarlem", 52.3873878, 4.6462194),
    //HOORN("Hoorn",52.641460,5.056810),
    JOHANNESBURG("Johannesburg", -26.2041028, 28.0473051),
    LEEUWARDEN("Leeuwarden", 53.2012334, 5.7999133),
    LELYSTAD("Lelystad", 52.518537, 5.471422),
    MAASTRICHT("Maastricht", 50.849375, 5.694609),
    MIDDELBURG("Middelburg", 51.4987962, 3.610998),
    //NORGE("Norge",79.349848,21.066975),
    //ROTTERDAM("Rotterdam",51.924419,4.477733),
    SYDNEY("Sydney", -33.8688197, 151.2092955),
    SVEAGRUVA(" Sveagruva ", 77.893123, 16.683988),
    UTRECHT("Utrecht", 52.09061, 5.12143),
    WASHINGTON_DC("Washington DC", 38.9071923, -77.0368707),
    ZWOLLE("Zwolle", 52.5167747, 6.0830219),
    ;



    companion object {

        fun CityName(city: String): Locations {
            enumValues<Locations>().forEach { if(it.cityName==city ) return it }
            return AMSTERDAM
        }
    }

}