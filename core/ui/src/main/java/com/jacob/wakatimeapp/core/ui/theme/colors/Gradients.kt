@file:Suppress("MagicNumber")

package com.jacob.wakatimeapp.core.ui.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkGradients

@Immutable
data class Gradient(
    val startColor: Color,
    val endColor: Color,
    val opacity: Int = 100,
    val onStartColor: Color = startColor,
    val onEndColor: Color = endColor,
) {
    val colorList get() = listOf(startColor, endColor)
}

@Immutable
data class Gradients(
    val primary: Gradient,
    val secondary: Gradient,
    val pinkCyanLight: Gradient,
    val purpleCyanDark: Gradient,
    val greenCyan: Gradient,
    val purpleCyan: Gradient,
    val redPurple: Gradient,
    val orangeYellow: Gradient,
)

val LocalGradients = staticCompositionLocalOf { DarkGradients }
