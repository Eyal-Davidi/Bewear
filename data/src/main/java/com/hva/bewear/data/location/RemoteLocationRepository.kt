package com.hva.bewear.data.location

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import com.hva.bewear.domain.location.LocationRepository

class RemoteLocationRepository(val locationService: LocationService) : LocationRepository{


    override suspend fun getLocation(text: String): List<String> {
        val location = ArrayList<String>()

            locationService.update(text)?.forEach {
                /*val name = it.name+ ", " + it.state+ ", " + it.country*/
                val name = it!!.getPrimaryText(StyleSpan(Typeface.BOLD)).toString()
                location.add(name) }

        return location
    }
}