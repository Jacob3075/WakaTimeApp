package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun StatsChip(
    gradient: Gradient,
    @DrawableRes iconId: Int,
    roundedCornerPercent: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    rotation: Float = 0f,
    chipContent: @Composable ColumnScope.() -> Unit,
) {
    val gradientBrush = Brush.horizontalGradient(gradient.colorList)
    val shape = RoundedCornerShape(roundedCornerPercent)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .background(gradientBrush, shape),
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(gradient.onEndColor),
            modifier = Modifier
                .padding(
                    end = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.extraSmall,
                )
                .size(size = 50.dp)
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    this.rotationX = rotation
                },
        )
        Column(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.small,
                )
                .graphicsLayer { this.rotationX = rotation },
            content = chipContent,
        )
    }
}

/**
 * [Rotating on Axis in 3D](https://www.youtube.com/watch?v=WdQUDHOwlgE&t=148s)
 * [Resetting Animation for each click](https://stackoverflow.com/questions/78620347/repeat-animation-when-user-clicks-jetpack-compose-android)
 * [Showing the back of the card correctly](https://medium.com/bilue/card-flip-animation-with-jetpack-compose-f60aaaad4ac9)
 */
@Composable
fun InteractableStatsChip(
    modifier: Modifier,
    gradient: Gradient,
    iconId: Int = MaterialTheme.assets.icons.time,
    frontContent: @Composable ColumnScope.() -> Unit = {},
    backContent: @Composable ColumnScope.() -> Unit = {},
) {
    var isFlipped by remember { mutableStateOf(false) }
    val rotationXAnimation = animateFloatAsState(targetValue = if (isFlipped) 180f else 0f)

    val contentToShow = remember(rotationXAnimation.value) {
        if (rotationXAnimation.value < 90f) frontContent else backContent
    }

    val innerRotation = remember(rotationXAnimation.value) {
        if (rotationXAnimation.value < 90f) 0f else 180f
    }

    StatsChip(
        gradient = gradient,
        iconId = iconId,
        roundedCornerPercent = 15,
        onClick = { isFlipped = !isFlipped },
        modifier = modifier.graphicsLayer {
            this.rotationX = rotationXAnimation.value
            this.cameraDistance = 8 * this.density
        },
        rotation = innerRotation,
        chipContent = { contentToShow() },
    )
}

@WtaPreviews
@Composable
private fun StatsChipPreview() = WakaTimeAppTheme {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            StatsChip(
                gradient = MaterialTheme.gradients.purpink,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 25,
                modifier = Modifier.weight(1f),
            ) {}
            StatsChip(
                gradient = MaterialTheme.gradients.amin,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 25,
                modifier = Modifier.weight(1f),
            ) {}
        }
    }
}
