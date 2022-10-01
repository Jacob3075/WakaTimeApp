package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
object Spacing {
    val extraSmall = 4.dp
    val small = 8.dp

    /**
     * [When a size small is too small and the size medium is too big you are a size smedium](https://english.stackexchange.com/a/163611)
     * */
    val sMedium = 12.dp
    val medium = 16.dp
    val lMedium = 24.dp
    val large = 32.dp
    val extraLarge = 64.dp
}

val LocalSpacing = staticCompositionLocalOf { Spacing }
