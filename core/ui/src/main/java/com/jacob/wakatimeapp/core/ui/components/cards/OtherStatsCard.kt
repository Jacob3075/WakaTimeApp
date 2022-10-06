package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient

@Composable
fun OtherStatsCard(
    gradient: Gradient,
    @DrawableRes iconId: Int,
    mainText: String,
    language: String,
    onClick: () -> Unit,
) = StatsCard(
    gradient = gradient,
    roundedCornerPercent = 25,
    iconId = iconId,
    mainText = mainText,
    text = language,
    iconOffset = 90.dp,
    iconSize = 70.dp,
    onClick = onClick
)
