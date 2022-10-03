package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.ui.R

private val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_extralight, FontWeight.ExtraLight),
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_extrabold, FontWeight.ExtraBold),
    Font(R.font.poppins_black, FontWeight.Black),
)

private val interFontFamily = FontFamily(
    Font(R.font.inter_extralight, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extrabold, FontWeight.ExtraBold),
    Font(R.font.inter_black, FontWeight.Black),
)

private val latoFontFamily = FontFamily(
    Font(R.font.lato_thin, FontWeight.Thin),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_black, FontWeight.Black),
)

val playfairDisplayFontFamily = FontFamily(
    Font(R.font.playfair_display),
)

val defaultTypography = Typography()
val Typography = Typography(
    defaultFontFamily = poppinsFontFamily,
    button = defaultTypography.button.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
)

val Typography.pageTitle: TextStyle
    get() = h3.copy(
        fontFamily = playfairDisplayFontFamily,
        fontSize = 56.sp,
        textAlign = TextAlign.Center,
        lineHeight = 60.sp,
    )

val Typography.sectionTitle
    get() = h5.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
    )

val Typography.sectionSubtitle
    get() = body2

val Typography.cardHeader
    get() = h6.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
    )

val Typography.cardSubtitle
    get() = body2.copy(
        fontWeight = FontWeight.Light,
    )

val Typography.cardContent
    get() = subtitle1.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
    )
