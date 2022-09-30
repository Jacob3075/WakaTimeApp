package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing internal constructor(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    /**
     * [When a size small is too small and the size medium is too big you are a size smedium](https://english.stackexchange.com/a/163611)
     * */
    val sMedium: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val lMedium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
