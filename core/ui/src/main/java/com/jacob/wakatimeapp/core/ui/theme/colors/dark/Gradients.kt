@file:Suppress("MagicNumber", "UnusedPrivateMember", "unused")

package com.jacob.wakatimeapp.core.ui.theme.colors.dark

import androidx.compose.ui.graphics.Color
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradients

private val flareStart = Color(0xFFFFB4A7)
private val onFlareStart = Color(0xFF670400)
private val flareStartContainer = Color(0xFF910900)
private val onFlareStartContainer = Color(0xFFFFDAD4)

private val flareEnd = Color(0xFFFFBA33)
private val onFlareEnd = Color(0xFF422C00)
private val flareEndContainer = Color(0xFF5F4100)
private val onFlareEndContainer = Color(0xFFFFDEAB)

private val shifterStart = Color(0xFFFFADE0)
private val onShifterStart = Color(0xFF60004D)
private val shifterStartContainer = Color(0xFF801868)
private val onShifterStartContainer = Color(0xFFFFD8ED)

private val shifterEnd = Color(0xFFFFB2BA)
private val onShifterEnd = Color(0xFF670020)
private val shifterEndContainer = Color(0xFF910030)
private val onShifterEndContainer = Color(0xFFFFD9DC)

private val quepalStart = Color(0xFF51DBCD)
private val onQuepalStart = Color(0xFF003733)
private val quepalStartContainer = Color(0xFF00504A)
private val onQuepalStartContainer = Color(0xFF72F8E9)

private val quepalEnd = Color(0xFF23E373)
private val onQuepalEnd = Color(0xFF003917)
private val quepalEndContainer = Color(0xFF005224)
private val onQuepalEndContainer = Color(0xFF64FF92)

private val tealLoveStart = Color(0xFF87D988)
private val onTealLoveStart = Color(0xFF00390E)
private val tealLoveStartContainer = Color(0xFF005318)
private val onTealLoveStartContainer = Color(0xFFA2F6A1)

private val tealLoveEnd = Color(0xFF00E1A6)
private val onTealLoveEnd = Color(0xFF003827)
private val tealLoveEndContainer = Color(0xFF00513A)
private val onTealLoveEndContainer = Color(0xFF3DFFC0)

private val facebookMessengerStart = Color(0xFF6DD2FF)
private val onFacebookMessengerStart = Color(0xFF003547)
private val facebookMessengerStartContainer = Color(0xFF004D65)
private val onFacebookMessengerStartContainer = Color(0xFFBFE9FF)

private val facebookMessengerEnd = Color(0xFFAFC6FF)
private val onFacebookMessengerEnd = Color(0xFF002D6D)
private val facebookMessengerEndContainer = Color(0xFF00429A)
private val onFacebookMessengerEndContainer = Color(0xFFD9E2FF)

private val reefStart = Color(0xFF47D6FF)
private val onReefStart = Color(0xFF003543)
private val reefStartContainer = Color(0xFF004E60)
private val onReefStartContainer = Color(0xFFB6EBFF)

private val reefEnd = Color(0xFFA9C7FF)
private val onReefEnd = Color(0xFF003063)
private val reefEndContainer = Color(0xFF00468C)
private val onReefEndContainer = Color(0xFFD6E3FF)

private val aminStart = Color(0xFFDDB7FF)
private val onAminStart = Color(0xFF4A0080)
private val aminStartContainer = Color(0xFF6900B3)
private val onAminStartContainer = Color(0xFFF0DBFF)

private val aminEnd = Color(0xFFCABEFF)
private val onAminEnd = Color(0xFF30009B)
private val aminEndContainer = Color(0xFF4700D7)
private val onAminEndContainer = Color(0xFFE6DEFF)

private val neuromancerStart = Color(0xFFFFAEDD)
private val onNeuromancerStart = Color(0xFF600049)
private val neuromancerStartContainer = Color(0xFF880069)
private val onNeuromancerStartContainer = Color(0xFFFFD8EB)

private val neuromancerEnd = Color(0xFFFFB0CE)
private val onNeuromancerEnd = Color(0xFF64003A)
private val neuromancerEndContainer = Color(0xFF8C0054)
private val onNeuromancerEndContainer = Color(0xFFFFD9E5)

private val purpinkStart = Color(0xFFD5BAFF)
private val onPurpinkStart = Color(0xFF42008A)
private val purpinkStartContainer = Color(0xFF5E00C1)
private val onPurpinkStartContainer = Color(0xFFECDCFF)

private val purpinkEnd = Color(0xFFFBAAFF)
private val onPurpinkEnd = Color(0xFF580064)
private val purpinkEndContainer = Color(0xFF7C008D)
private val onPurpinkEndContainer = Color(0xFFFFD6FC)

internal val DarkGradients = Gradients(
    flare = Gradient(
        startColor = flareStart,
        endColor = flareEnd,
        onStartColor = onFlareStart,
        onEndColor = onFlareEnd,
    ),
    shifter = Gradient(
        startColor = shifterStart,
        endColor = shifterEnd,
        onStartColor = onShifterStart,
        onEndColor = onShifterEnd,
    ),
    quepal = Gradient(
        startColor = quepalStart,
        endColor = quepalEnd,
        onStartColor = onQuepalStart,
        onEndColor = onQuepalEnd,
    ),
    tealLove = Gradient(
        startColor = tealLoveStart,
        endColor = tealLoveEnd,
        onStartColor = onTealLoveStart,
        onEndColor = onTealLoveEnd,
    ),
    facebookMessenger = Gradient(
        startColor = facebookMessengerStart,
        endColor = facebookMessengerEnd,
        onStartColor = onFacebookMessengerStart,
        onEndColor = onFacebookMessengerEnd,
    ),
    reef = Gradient(
        startColor = reefStart,
        endColor = reefEnd,
        onStartColor = onReefStart,
        onEndColor = onReefEnd,
    ),
    amin = Gradient(
        startColor = aminStart,
        endColor = aminEnd,
        onStartColor = onAminStart,
        onEndColor = onAminEnd,
    ),
    neuromancer = Gradient(
        startColor = neuromancerStart,
        endColor = neuromancerEnd,
        onStartColor = onNeuromancerStart,
        onEndColor = onNeuromancerEnd,
    ),
    purpink = Gradient(
        startColor = purpinkStart,
        endColor = purpinkEnd,
        onStartColor = onPurpinkStart,
        onEndColor = onPurpinkEnd,
    ),
)
