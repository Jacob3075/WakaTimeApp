@file:Suppress("MagicNumber", "UnusedPrivateMember", "unused")

package com.jacob.wakatimeapp.core.ui.theme.colors.light

import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradients

private val flareStart = Color(0xFFBE0F00)
private val onFlareStart = Color(0xFFFFFFFF)
private val flareStartContainer = Color(0xFFFFDAD4)
private val onFlareStartContainer = Color(0xFF400200)

private val flareEnd = Color(0xFF7E5700)
private val onFlareEnd = Color(0xFFFFFFFF)
private val flareEndContainer = Color(0xFFFFDEAB)
private val onFlareEndContainer = Color(0xFF281900)

private val shifterStart = Color(0xFF9D3481)
private val onShifterStart = Color(0xFFFFFFFF)
private val shifterStartContainer = Color(0xFFFFD8ED)
private val onShifterStartContainer = Color(0xFF3B002E)

private val shifterEnd = Color(0xFFBD0041)
private val onShifterEnd = Color(0xFFFFFFFF)
private val shifterEndContainer = Color(0xFFFFD9DC)
private val onShifterEndContainer = Color(0xFF400010)

private val tealLoveEnd = Color(0xFF006C4E)
private val onTealLoveEnd = Color(0xFFFFFFFF)
private val tealLoveEndContainer = Color(0xFF3DFFC0)
private val onTealLoveEndContainer = Color(0xFF002115)

private val quepalStart = Color(0xFF006A62)
private val onQuepalStart = Color(0xFFFFFFFF)
private val quepalStartContainer = Color(0xFF72F8E9)
private val onQuepalStartContainer = Color(0xFF00201D)

private val quepalEnd = Color(0xFF006D32)
private val onQuepalEnd = Color(0xFFFFFFFF)
private val quepalEndContainer = Color(0xFF64FF92)
private val onQuepalEndContainer = Color(0xFF00210B)

private val tealLoveStart = Color(0xFF196D29)
private val onTealLoveStart = Color(0xFFFFFFFF)
private val tealLoveStartContainer = Color(0xFFA2F6A1)
private val onTealLoveStartContainer = Color(0xFF002105)

private val facebookMessengerStart = Color(0xFF006685)
private val onFacebookMessengerStart = Color(0xFFFFFFFF)
private val facebookMessengerStartContainer = Color(0xFFBFE9FF)
private val onFacebookMessengerStartContainer = Color(0xFF001F2A)

private val facebookMessengerEnd = Color(0xFF0058C9)
private val onFacebookMessengerEnd = Color(0xFFFFFFFF)
private val facebookMessengerEndContainer = Color(0xFFD9E2FF)
private val onFacebookMessengerEndContainer = Color(0xFF001944)

private val reefStart = Color(0xFF00677F)
private val onReefStart = Color(0xFFFFFFFF)
private val reefStartContainer = Color(0xFFB6EBFF)
private val onReefStartContainer = Color(0xFF001F28)

private val reefEnd = Color(0xFF055DB6)
private val onReefEnd = Color(0xFFFFFFFF)
private val reefEndContainer = Color(0xFFD6E3FF)
private val onReefEndContainer = Color(0xFF001B3D)

private val aminStart = Color(0xFF8621DA)
private val onAminStart = Color(0xFFFFFFFF)
private val aminStartContainer = Color(0xFFF0DBFF)
private val onAminStartContainer = Color(0xFF2C0050)

private val aminEnd = Color(0xFF5F32F3)
private val onAminEnd = Color(0xFFFFFFFF)
private val aminEndContainer = Color(0xFFE6DEFF)
private val onAminEndContainer = Color(0xFF1C0062)

private val neuromancerStart = Color(0xFFB1018A)
private val onNeuromancerStart = Color(0xFFFFFFFF)
private val neuromancerStartContainer = Color(0xFFFFD8EB)
private val onNeuromancerStartContainer = Color(0xFF3B002C)

private val neuromancerEnd = Color(0xFFB3166E)
private val onNeuromancerEnd = Color(0xFFFFFFFF)
private val neuromancerEndContainer = Color(0xFFFFD9E5)
private val onNeuromancerEndContainer = Color(0xFF3E0022)

private val purpinkStart = Color(0xFF7D00FA)
private val onPurpinkStart = Color(0xFFFFFFFF)
private val purpinkStartContainer = Color(0xFFECDCFF)
private val onPurpinkStartContainer = Color(0xFF270057)

private val purpinkEnd = Color(0xFFA300B9)
private val onPurpinkEnd = Color(0xFFFFFFFF)
private val purpinkEndContainer = Color(0xFFFFD6FC)
private val onPurpinkEndContainer = Color(0xFF36003E)

internal val LightGradients = Gradients(
    flare = Gradient(
        startColor = Gradients.flareStart,
        endColor = Gradients.flareEnd,
        onStartColor = onFlareStart,
        onEndColor = onFlareEnd,
    ),
    shifter = Gradient(
        startColor = Gradients.shifterStart,
        endColor = Gradients.shifterEnd,
        onStartColor = onShifterStart,
        onEndColor = onShifterEnd,
    ),
    quepal = Gradient(
        startColor = Gradients.quepalStart,
        endColor = Gradients.quepalEnd,
        onStartColor = onQuepalStart,
        onEndColor = onQuepalEnd,
    ),
    tealLove = Gradient(
        startColor = Gradients.tealLoveStart,
        endColor = Gradients.tealLoveEnd,
        onStartColor = onTealLoveStart,
        onEndColor = onTealLoveEnd,
    ),
    facebookMessenger = Gradient(
        startColor = Gradients.facebookMessengerStart,
        endColor = Gradients.facebookMessengerEnd,
        onStartColor = onFacebookMessengerStart,
        onEndColor = onFacebookMessengerEnd,
    ),
    reef = Gradient(
        startColor = Gradients.reefStart,
        endColor = Gradients.reefEnd,
        onStartColor = onReefStart,
        onEndColor = onReefEnd,
    ),
    amin = Gradient(
        startColor = Gradients.aminStart,
        endColor = Gradients.aminEnd,
        onStartColor = onAminStart,
        onEndColor = onAminEnd,
    ),
    neuromancer = Gradient(
        startColor = Gradients.neuromancerStart,
        endColor = Gradients.neuromancerEnd,
        onStartColor = onNeuromancerStart,
        onEndColor = onNeuromancerEnd,
    ),
    purpink = Gradient(
        startColor = Gradients.purpinkStart,
        endColor = Gradients.purpinkEnd,
        onStartColor = onPurpinkStart,
        onEndColor = onPurpinkEnd,
    ),
)
