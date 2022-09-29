@file:Suppress("MagicNumber")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class Gradient(val startColor: Color, val endColor: Color, val opacity: Int = 100)

@Immutable
data class Gradients internal constructor(
    val primary: Gradient = Gradient(Color(0xFF3F67E0), Color(0xFF65D1E2)),
    val secondary: Gradient = Gradient(Color(0xFF3F55E0), Color(0xFF5EC2D2), 85),
    val purpleCyanLight: Gradient = Gradient(Color(0xFFCC1FBB), Color(0xFF6FEBFF)),
    val purpleCyanDark: Gradient = Gradient(Color(0xFF921FC9), Color(0xFF5EC2D2)),
    val greenCyan: Gradient = Gradient(Color(0xFF18A963), Color(0xFF65D0E1)),
    val blueCyan: Gradient = Gradient(Color(0xFF7B61FF), Color(0xFF63CFE1)),
    val redPurple: Gradient = Gradient(Color(0xFFEB4F4F), Color(0xFFDC52B5)),
    val orangeYellow: Gradient = Gradient(Color(0xFFFF9900), Color(0xFFE8C754)),
)

val LocalGradients = staticCompositionLocalOf { Gradients() }
