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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.components.rememberFlippableState
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient

@Composable
fun StatsCard(
    gradient: Gradient,
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    roundedCornerPercent: Int = 25,
    iconOffset: Dp = 50.dp,
    iconSize: Dp = 80.dp,
    rotation: Float = 0f,
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
        modifier = modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .background(cardGradient, cardShape),
        content = {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                colorFilter = ColorFilter.tint(gradient.onEndColor),
               alpha = 0.15f,
                modifier = Modifier
                    .padding(start = iconOffset)
                    .size(iconSize)
                    .graphicsLayer { this.rotationX = rotation },
            )
            Box(Modifier.graphicsLayer { this.rotationX = rotation }, content = cardContent)
        },
    )
}

@Composable
fun FlippableStatsCard(
    gradient: Gradient,
    modifier: Modifier = Modifier,
    iconId: Int = MaterialTheme.assets.icons.time,
    roundedCornerPercent: Int = 25,
    frontContent: @Composable BoxScope.() -> Unit = {},
    backContent: @Composable BoxScope.() -> Unit = {},
) {
    val flippableState = rememberFlippableState(frontContent, backContent)

    StatsCard(
        gradient = gradient,
        iconId = iconId,
        roundedCornerPercent = roundedCornerPercent,
        onClick = { flippableState.isFlipped.value = !flippableState.isFlipped.value },
        modifier = modifier.graphicsLayer {
            this.rotationX = flippableState.rotationAnimationValue.value
            this.cameraDistance = 8 * this.density
        },
        rotation = flippableState.innerRotation,
        cardContent = flippableState.contentToShow,
    )
}
