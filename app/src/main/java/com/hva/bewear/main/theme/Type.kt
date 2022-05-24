package com.hva.bewear.main.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hva_bewear.R

val nunito = FontFamily(
    //Font(R.font.nunito_black, weight = FontWeight.Normal, style = FontStyle.Normal),
    //Font(R.font.nunito_black_italic),
    Font(R.font.nunito_bold, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(R.font.nunito_bold_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.nunito_extra_bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(R.font.nunito_extra_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    //Font(R.font.nunito_extra_light),
    //Font(R.font.nunito_extra_light_italic),
    //Font(R.font.nunito_italic),
    //Font(R.font.nunito_light),
    //Font(R.font.nunito_light_italic),
    //Font(R.font.nunito_medium),
    //Font(R.font.nunito_medium_italic),
    //Font(R.font.nunito_regular),
    Font(R.font.nunito_semi_bold, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(R.font.nunito_semi_bold_italic, weight = FontWeight.Light, style = FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)