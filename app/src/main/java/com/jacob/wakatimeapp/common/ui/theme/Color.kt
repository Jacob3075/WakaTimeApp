package com.jacob.wakatimeapp.common.ui.theme

import androidx.compose.ui.graphics.Color

data class Gradient(val startColor: Color, val endColor: Color, val opacity: Int = 100)

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

object Colors {
    val AppBG = Color(0xFF121212)
    val CardBGPrimary = Color(0xFF272727)
    val CardBGSecondary = Color(0xFF2E2E2E)
    val AccentIcons = Color(0xFF6FEBFF)
    val AccentText = Color(0xFF8CE3E3)
}

object Gradients {
    val primary = Gradient(Color(0xFF3F55E0), Color(0xFF5EC2D2))
    val secondary = Gradient(Color(0xFF3F55E0), Color(0xFF5EC2D2), 85)
    val purpleCyanLight = Gradient(Color(0xFFCC1FBB), Color(0xFF6FEBFF))
    val purpleCyanDark = Gradient(Color(0xFF921FC9), Color(0xFF5EC2D2))
    val greenCyan = Gradient(Color(0xFF18A963), Color(0xFF65D0E1))
    val blueCyan = Gradient(Color(0xFF7B61FF), Color(0xFF63CFE1))
    val redPurple = Gradient(Color(0xFFEB4F4F), Color(0xFFDC52B5))
    val orangeYellow = Gradient(Color(0xFFFF9900), Color(0xFFE8C754))
}
