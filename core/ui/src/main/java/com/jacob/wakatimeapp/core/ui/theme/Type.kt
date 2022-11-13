package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.material3.Typography
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

val playfairDisplayFontFamily = FontFamily(
    Font(R.font.playfair_display),
)

val defaultTypography = Typography()

val Typography = defaultTypography.let {
    fun TextStyle.setDefaultFontFamily() = copy(fontFamily = poppinsFontFamily)

    Typography(
        displayLarge = it.displayLarge.setDefaultFontFamily(),
        displayMedium = it.displayMedium.setDefaultFontFamily(),
        displaySmall = it.displaySmall.setDefaultFontFamily(),
        headlineLarge = it.headlineLarge.setDefaultFontFamily(),
        headlineMedium = it.headlineMedium.setDefaultFontFamily(),
        headlineSmall = it.headlineSmall.setDefaultFontFamily(),
        titleLarge = it.titleLarge.setDefaultFontFamily(),
        titleMedium = it.titleMedium.setDefaultFontFamily(),
        titleSmall = it.titleSmall.setDefaultFontFamily(),
        bodyLarge = it.bodyLarge.setDefaultFontFamily(),
        bodyMedium = it.bodyMedium.setDefaultFontFamily(),
        bodySmall = it.bodySmall.setDefaultFontFamily(),
        labelLarge = it.labelLarge.setDefaultFontFamily(),
        labelMedium = it.labelMedium.setDefaultFontFamily(),
        labelSmall = it.labelSmall.setDefaultFontFamily(),
    )
}

val Typography.button: TextStyle
    get() = bodySmall.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

val Typography.pageTitle: TextStyle
    get() = displayMedium.copy(
        fontFamily = playfairDisplayFontFamily,
        fontSize = 56.sp,
        textAlign = TextAlign.Center,
        lineHeight = 60.sp,
    )

val Typography.sectionTitle
    get() = headlineSmall.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
    )

val Typography.sectionSubtitle
    get() = labelLarge

val Typography.cardHeader
    get() = titleLarge.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
    )

val Typography.cardSubtitle
    get() = bodyMedium.copy(
        fontWeight = FontWeight.Light,
    )

val Typography.cardContent
    get() = bodyLarge.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
    )
