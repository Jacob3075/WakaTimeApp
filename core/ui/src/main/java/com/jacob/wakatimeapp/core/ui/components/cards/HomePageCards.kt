package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.cardContent
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients

@Composable
fun TimeSpentCard(
    statsType: String,
    time: Time,
    gradient: Gradient,
    @DrawableRes iconId: Int,
    roundedCornerPercent: Int,
    onClick: () -> Unit,
) = StatsCard(
    gradient = gradient,
    onClick = onClick,
    roundedCornerPercent = roundedCornerPercent,
    cardContent = {
        CardContent(
            statsType = statsType,
            statsValue = "${time.hours}H, ${time.minutes}M",
            iconId = iconId,
            gradient = gradient,
        )
    },
)

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
    cardContent = {
        CardContent(
            statsType = statsType,
            statsValue = language,
            iconOffset = 90.dp,
            iconSize = 70.dp,
            iconId = iconId,
            gradient = gradient,
        )
    },
)

@Composable
private fun BoxScope.CardContent(
    statsType: String,
    statsValue: String,
    @DrawableRes iconId: Int,
    statsTypeWeight: Float = 1f,
    statsValueWeight: Float = 0.5f,
    iconOffset: Dp = 50.dp,
    iconSize: Dp = 80.dp,
    gradient: Gradient,
) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = "",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .padding(start = iconOffset)
            .size(iconSize),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
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

@WtaPreviews
@Composable
private fun TimeSpentCardPreview() = WakaTimeAppTheme {
    Surface {
        Row {
            TimeSpentCard(
                statsType = "Total Time Spent Today",
                time = Time(42, 22, 0f),
                gradient = MaterialTheme.gradients.facebookMessenger,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 25,
            ) {}
        }
    }
}
