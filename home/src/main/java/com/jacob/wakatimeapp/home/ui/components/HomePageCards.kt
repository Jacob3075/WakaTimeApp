package com.jacob.wakatimeapp.home.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.ui.components.cards.StatsCard
import com.jacob.wakatimeapp.core.ui.theme.cardContent
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun OtherStatsCard(
    statsType: String,
    language: String,
    gradient: Gradient,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
) = StatsCard(
    gradient = gradient,
    onClick = onClick,
    roundedCornerPercent = 25,
    iconSize = 70.dp,
    iconId = iconId,
    iconOffset = 90.dp,
    cardContent = {
        CardContent(
            statsType = statsType,
            statsValue = language,
            gradient = gradient,
        )
    },
)

@Composable
internal fun BoxScope.CardContent(
    statsType: String,
    statsValue: String,
    statsTypeWeight: Float = 1f,
    statsValueWeight: Float = 0.5f,
    gradient: Gradient,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
    ) {
        Text(
            text = statsType,
            maxLines = 2,
            modifier = Modifier.weight(statsTypeWeight, true),
            style = MaterialTheme.typography.cardContent,
            color = gradient.onStartColor,
        )
        Text(
            text = statsValue,
            modifier = Modifier.weight(statsValueWeight, true),
            textAlign = TextAlign.End,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
            color = gradient.onEndColor,
        )
    }
}
