package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.core.ui.theme.Gradient

@Composable
fun OtherStatsCard(
    gradient: Gradient,
    roundedCornerPercent: Int,
    @DrawableRes iconId: Int,
    mainText: String,
    language: String,
    onClick: () -> Unit,
) = StatsCard(
    gradient = gradient,
    roundedCornerPercent = roundedCornerPercent,
    iconId = iconId,
    mainText = mainText,
    text = language,
    weights = Pair(1f, 0.5f),
    iconOffset = 90,
    iconSize = 70,
    onClick = onClick,
)
