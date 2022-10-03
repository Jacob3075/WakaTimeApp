@file:Suppress("MagicNumber")

package com.jacob.wakatimeapp.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class Gradient(val startColor: Color, val endColor: Color, val opacity: Int = 100)

@Immutable
object Gradients {
    val primary = Gradient(Color(0xFF3F67E0), Color(0xFF65D1E2))
    val secondary = Gradient(Color(0xFF3F55E0), Color(0xFF5EC2D2), 85)
    val purpleCyanLight = Gradient(Color(0xFFCC1FBB), Color(0xFF6FEBFF))
    val purpleCyanDark = Gradient(Color(0xFF921FC9), Color(0xFF5EC2D2))
    val greenCyan = Gradient(Color(0xFF18A963), Color(0xFF65D0E1))
    val blueCyan = Gradient(Color(0xFF7B61FF), Color(0xFF63CFE1))
    val redPurple = Gradient(Color(0xFFEB4F4F), Color(0xFFDC52B5))
    val orangeYellow = Gradient(Color(0xFFFF9900), Color(0xFFE8C754))
}

val LocalGradients = staticCompositionLocalOf { Gradients }
