@file:Suppress("MagicNumber")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

internal val DarkColorPalette = darkColors(
    primary = Colors.AccentText,
    secondary = Colors.AccentIcons,
    background = Colors.AppBG,
    surface = Colors.CardBackground
)

internal val LightColorPalette = lightColors(
    primary = Colors.AccentText,
    secondary = Colors.AccentIcons,
)

private object Colors {
    val AppBG = Color(0xFF121212)
    val CardBackground = Color(0xFF272727)
    val AccentIcons = Color(0xFF6FEBFF)
    val AccentText = Color(0xFF8CE3E3)
}
