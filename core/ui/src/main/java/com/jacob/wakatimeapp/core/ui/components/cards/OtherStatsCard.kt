package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient

@Composable
fun OtherStatsCard(
    statsType: String,
    language: String,
    gradient: Gradient,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
) = StatsCard(
    statsType = statsType,
    statsValue = language,
    gradient = gradient,
    iconId = iconId,
    onClick = onClick,
    roundedCornerPercent = 25,
    iconOffset = 90.dp,
    iconSize = 70.dp
)
