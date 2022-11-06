@file:Suppress("MagicNumber", "UnusedPrivateMember", "unused")

package com.jacob.wakatimeapp.core.ui.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkGradients
import com.jacob.wakatimeapp.core.ui.theme.colors.dark.DarkNewGradients

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

@Immutable
data class NewGradients(
    val flare: Gradient,
    val shifter: Gradient,
    val quepal: Gradient,
    val tealLove: Gradient,
    val facebookMessenger: Gradient,
    val reef: Gradient,
    val amin: Gradient,
    val neuromancer: Gradient,
    val purpink: Gradient,
) {

    /**
     * [Source](https://uigradients.com)
     */
    companion object Seed {
        val flareStart = Color(0xFFF12711)
        val flareEnd = Color(0xFFF5AF19)

        val shifterStart = Color(0xFFBC4E9C)
        val shifterEnd = Color(0xFFF80759)

        val quepalStart = Color(0xFF11998E)
        val quepalEnd = Color(0xFF38EF7D)

        val tealLoveStart = Color(0xFFAAFFA9)
        val tealLoveEnd = Color(0xFF11FFBD)

        val facebookMessengerStart = Color(0xFF00C6FF)
        val facebookMessengerEnd = Color(0xFF0072FF)

        val reefStart = Color(0xFF00D2FF)
        val reefEnd = Color(0xFF3A7BD5)

        val aminStart = Color(0xFF8E2DE2)
        val aminEnd = Color(0xFF4A00E0)

        val neuromancerStart = Color(0xFFF953C6)
        val neuromancerEnd = Color(0xFFB91D73)

        val purpinkStart = Color(0xFF7F00FF)
        val purpinkEnd = Color(0xFFE100FF)
    }
}

val LocalGradients = staticCompositionLocalOf { DarkGradients }

val LocalNewGradients = staticCompositionLocalOf { DarkNewGradients }

/**
 * [Source](https://jemimaabu.github.io/random-gradient-generator/)
 */
private fun abc() {
    Gradient(startColor = Color(0xFFF64CEA), Color(0xFF8605f1))
    Gradient(startColor = Color(0xFF4565C1), Color(0xFFa654de))
    Gradient(startColor = Color(0xFFEB5831), Color(0xFF933caf))
    Gradient(startColor = Color(0xFF8314B9), Color(0xFF96477c))
    Gradient(startColor = Color(0xFF38BC36), Color(0xFF74fbbc))
    Gradient(startColor = Color(0xFFD7954E), Color(0xFFe4e121))
    Gradient(startColor = Color(0xFF240F88), Color(0xFF5e3fe0))
    Gradient(startColor = Color(0xFF444ECF), Color(0xFF26f6c3))
    Gradient(startColor = Color(0xFF677FC4), Color(0xFF5bced7))
}
