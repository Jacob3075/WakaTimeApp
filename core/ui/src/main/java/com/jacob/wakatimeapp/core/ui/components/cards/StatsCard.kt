package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient

@Composable
fun StatsCard(
    gradient: Gradient,
    onClick: () -> Unit,
    roundedCornerPercent: Int = 25,
    @DrawableRes iconId: Int,
    iconOffset: Dp = 50.dp,
    iconSize: Dp = 80.dp,
    cardContent: @Composable BoxScope.() -> Unit = {},
) {
    val cardGradient = Brush.horizontalGradient(
        listOf(
            gradient.startColor,
            gradient.endColor,
        ),
    )
    val cardShape = RoundedCornerShape(roundedCornerPercent)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(cardGradient, cardShape),
        content = {

            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(start = iconOffset)
                    .size(iconSize),
            )
            cardContent()
        },
    )
}
