package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
internal fun StatsCard(
    gradient: Gradient,
    onClick: () -> Unit,
    roundedCornerPercent: Int = 25,
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
            .clickable { onClick() }
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = MaterialTheme.spacing.lMedium),
        content = cardContent,
    )
}
