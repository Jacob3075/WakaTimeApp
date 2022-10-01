package com.jacob.wakatimeapp.core.ui.theme.assets

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
object Assets {
    val animations: Animations = Animations
    val illustrations: Illustrations = Illustrations
    val icons: Icons = Icons
}

val LocalAssets = staticCompositionLocalOf { Assets }
