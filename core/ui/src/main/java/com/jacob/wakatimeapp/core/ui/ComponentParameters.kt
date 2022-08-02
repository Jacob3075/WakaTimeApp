package com.jacob.wakatimeapp.core.ui

import androidx.annotation.DrawableRes
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.Gradient

data class TimeSpentCardParameters(
    val gradient: Gradient,
    val roundedCornerPercent: Int,
    @DrawableRes val iconId: Int,
    val mainText: String,
    val time: Time,
    val onClick: () -> Unit,
)

data class OtherStatsCardParameters(
    val gradient: Gradient,
    val roundedCornerPercent: Int,
    @DrawableRes val iconId: Int,
    val mainText: String,
    val language: String,
    val onClick: () -> Unit,
)

data class StatsCardParameters(
    val gradient: Gradient,
    val roundedCornerPercent: Int = 25,
    @DrawableRes val iconId: Int,
    val mainText: String,
    val text: String,
    val weights: Pair<Float, Float> = Pair(1f, 1f),
    val iconOffset: Int = 50,
    val iconSize: Int = 80,
    val onClick: () -> Unit,
)
