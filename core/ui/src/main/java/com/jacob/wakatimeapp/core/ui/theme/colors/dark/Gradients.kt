@file:Suppress("MagicNumber", "UnusedPrivateMember", "unused")

package com.jacob.wakatimeapp.core.ui.theme.colors.dark

import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradients

private val BLUE = Color(0xFF3F67E0)
private val CYAN = Color(0xFF65D1E2)
private val DARK_PINK = Color(0xFFCC1FBB)
private val GREEN = Color(0xFF18A963)
private val PURPLE = Color(0xFF7B61FF)
private val RED = Color(0xFFEB4F4F)
private val PINK = Color(0xFFDC52B5)
private val DARK_PURPLE = Color(0xFF921FC9)
private val CYAN_ALT = Color(0xFF5EC2D2)
private val ORANGE = Color(0xFFFF9900)
private val YELLOW = Color(0xFFE8C754)

private val orange = Color(0xFFFFB86F)
private val onOrange = Color(0xFF4A2800)
private val orangeContainer = Color(0xFF693C00)
private val onOrangeContainer = Color(0xFFFFDCBD)

private val yellow = Color(0xFFE6C449)
private val onYellow = Color(0xFF3B2F00)
private val yellowContainer = Color(0xFF564500)
private val onYellowContainer = Color(0xFFFFE07D)

private val blue = Color(0xFFB6C4FF)
private val onBlue = Color(0xFF00287D)
private val blueContainer = Color(0xFF003BB0)
private val onBlueContainer = Color(0xFFDCE1FF)

private val cyan = Color(0xFF4FD8EC)
private val onCyan = Color(0xFF00363D)
private val cyanContainer = Color(0xFF004F58)
private val onCyanContainer = Color(0xFF99F0FF)

private val darkPink = Color(0xFFFFACEB)
private val onDarkPink = Color(0xFF5D0055)
private val darkPinkContainer = Color(0xFF840079)
private val onDarkPinkContainer = Color(0xFFFFD7F1)

private val green = Color(0xFF5EDE92)
private val onGreen = Color(0xFF00391D)
private val greenContainer = Color(0xFF00522C)
private val onGreenContainer = Color(0xFF7CFBAC)

private val purple = Color(0xFFC9BFFF)
private val onPurple = Color(0xFF2E009C)
private val purpleContainer = Color(0xFF441CC8)
private val onPurpleContainer = Color(0xFFE5DEFF)

private val red = Color(0xFFFFB3AE)
private val onRed = Color(0xFF68000D)
private val redContainer = Color(0xFF910618)
private val onRedContainer = Color(0xFFFFDAD7)

private val pink = Color(0xFFFFADE0)
private val onPink = Color(0xFF60004C)
private val pinkContainer = Color(0xFF87006C)
private val onPinkContainer = Color(0xFFFFD8EC)

private val darkPurple = Color(0xFFE8B3FF)
private val onDarkPurple = Color(0xFF500074)
private val darkPurpleContainer = Color(0xFF7200A3)
private val onDarkPurpleContainer = Color(0xFFF6D9FF)

private val cyanAlt = Color(0xFF50D8EC)
private val onCyanAlt = Color(0xFF00363D)
private val cyanAltContainer = Color(0xFF004F58)
private val onCyanAltContainer = Color(0xFF99F0FF)

val DarkGradients = Gradients(
    primary = Gradient(
        startColor = blue,
        endColor = cyan,
        onStartColor = onBlue,
        onEndColor = onCyan
    ),
    secondary = Gradient(
        startColor = Color(0xFF3F55E0),
        endColor = Color(0xFF5EC2D2),
        opacity = 85
    ),
    pinkCyanLight = Gradient(
        startColor = pink,
        endColor = cyan,
        onStartColor = onPink,
        onEndColor = onCyan
    ),
    purpleCyanDark = Gradient(
        startColor = darkPurple,
        endColor = cyanAlt,
        onStartColor = onDarkPurple,
        onEndColor = onCyanAlt
    ),
    greenCyan = Gradient(
        startColor = green,
        endColor = cyan,
        onStartColor = onGreen,
        onEndColor = onCyan
    ),
    purpleCyan = Gradient(
        startColor = purple,
        endColor = cyan,
        onStartColor = onPurple,
        onEndColor = onCyan
    ),
    redPurple = Gradient(
        startColor = red,
        endColor = purple,
        onStartColor = onRed,
        onEndColor = onPurple
    ),
    orangeYellow = Gradient(
        startColor = orange,
        endColor = yellow,
        onStartColor = onOrange,
        onEndColor = onYellow
    )
)
