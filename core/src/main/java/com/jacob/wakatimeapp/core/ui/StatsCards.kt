package com.jacob.wakatimeapp.core.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.R
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme

@Composable
fun StatsCard(
    statsCardParameters: StatsCardParameters,
) {
    val cardGradient =
        Brush.horizontalGradient(listOf(statsCardParameters.gradient.startColor,
            statsCardParameters.gradient.endColor))
    val cardShape = RoundedCornerShape(statsCardParameters.roundedCornerPercent)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable { }
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = 22.dp)
    ) {
        Image(
            painter = painterResource(id = statsCardParameters.iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = statsCardParameters.iconOffset.dp)
                .size(statsCardParameters.iconSize.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statsCardParameters.mainText,
                maxLines = 2,
                modifier = Modifier.weight(statsCardParameters.weights.first, true),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                ),
            )
            Text(
                text = statsCardParameters.text,
                modifier = Modifier.weight(statsCardParameters.weights.second, true),
                textAlign = TextAlign.End,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}

@Composable
fun TimeSpentCard(
    timeSpentCardParameters: TimeSpentCardParameters,
) = StatsCard(
    statsCardParameters = StatsCardParameters(
        gradient = timeSpentCardParameters.gradient,
        roundedCornerPercent = timeSpentCardParameters.roundedCornerPercent,
        iconId = timeSpentCardParameters.iconId,
        mainText = timeSpentCardParameters.mainText,
        text = "${timeSpentCardParameters.time.hours}H, ${timeSpentCardParameters.time.minutes}M"
    ),
)

@Composable
fun TimeSpentChip() {
}

@Composable
fun OtherStatsCard(
    otherStatsCardParameters: OtherStatsCardParameters,
) = StatsCard(
    statsCardParameters = StatsCardParameters(
        gradient = otherStatsCardParameters.gradient,
        roundedCornerPercent = otherStatsCardParameters.roundedCornerPercent,
        iconId = otherStatsCardParameters.iconId,
        mainText = otherStatsCardParameters.mainText,
        text = otherStatsCardParameters.language,
        weights = Pair(1f, 0.5f),
        iconOffset = 90,
        iconSize = 70
    ),
)

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TimeSpentCardPreview() = WakaTimeAppTheme(darkTheme = true) {
    TimeSpentCard(
        timeSpentCardParameters = TimeSpentCardParameters(
            Gradients.primary,
            25,
            R.drawable.ic_time,
            "Total Time Spent Today",
            Time(42, 22, 0f)
        )
    )
}
