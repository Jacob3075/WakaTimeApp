@file:Suppress("MagicNumber", "UnusedPrivateMember")

package com.jacob.wakatimeapp.core.ui.theme.colors.light

import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradients

private val orange = Color(0xFF8A5100)
private val onOrange = Color(0xFFFFFFFF)
private val orangeContainer = Color(0xFFFFDCBD)
private val onOrangeContainer = Color(0xFF2C1600)

private val yellow = Color(0xFF725C00)
private val onYellow = Color(0xFFFFFFFF)
private val yellowContainer = Color(0xFFFFE07D)
private val onYellowContainer = Color(0xFF231B00)

private val blue = Color(0xFF3463E4)
private val onBlue = Color(0xFFFFFFFF)
private val blueContainer = Color(0xFFDCE1FF)
private val onBlueContainer = Color(0xFF00164F)

private val cyan = Color(0xFF26C6DA)
private val onCyan = Color(0xFFFFFFFF)
private val cyanContainer = Color(0xFF99F0FF)
private val onCyanContainer = Color(0xFF001F24)

private val darkPink = Color(0xFFAC009E)
private val onDarkPink = Color(0xFFFFFFFF)
private val darkPinkContainer = Color(0xFFFFD7F1)
private val onDarkPinkContainer = Color(0xFF390034)

private val green = Color(0xFF00A75D)
private val onGreen = Color(0xFFFFFFFF)
private val greenContainer = Color(0xFF7CFBAC)
private val onGreenContainer = Color(0xFF00210F)

private val purple = Color(0xFF6A4EE2)
private val onPurple = Color(0xFFFFFFFF)
private val purpleContainer = Color(0xFFE5DEFF)
private val onPurpleContainer = Color(0xFF1A0063)

private val red = Color(0xFFB4262C)
private val onRed = Color(0xFFFFFFFF)
private val RedContainer = Color(0xFFFFDAD7)
private val onRedContainer = Color(0xFF410005)

private val pink = Color(0xFFA82288)
private val onPink = Color(0xFFFFFFFF)
private val pinkContainer = Color(0xFFFFD8EC)
private val onPinkContainer = Color(0xFF3B002E)

private val darkPurple = Color(0xFF921FC9)
private val onDarkPurple = Color(0xFFFFFFFF)
private val darkPurpleContainer = Color(0xFFF6D9FF)
private val onDarkPurpleContainer = Color(0xFF310049)

private val cyanAlt = Color(0xFF006874)
private val onCyanAlt = Color(0xFFFFFFFF)
private val cyanAltContainer = Color(0xFF99F0FF)
private val onCyanAltContainer = Color(0xFF001F24)

val LightGradients = Gradients(
    primary = Gradient(
        startColor = blue,
        endColor = cyan,
        onStartColor = onBlue,
        onEndColor = onCyan
    ),
    secondary = Gradient(Color(0xFF3F55E0), Color(0xFF5EC2D2), 85),
    pinkCyanLight = Gradient(
        startColor = darkPink,
        endColor = cyan,
        onStartColor = onDarkPink,
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
