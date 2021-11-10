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
    parameters: StatsCardParameters,
) {
    val cardGradient =
        Brush.horizontalGradient(listOf(parameters.gradient.startColor,
            parameters.gradient.endColor))
    val cardShape = RoundedCornerShape(parameters.roundedCornerPercent)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .shadow(elevation = 10.dp, shape = cardShape)
            .clickable { parameters.onClick() }
            .fillMaxWidth()
            .background(cardGradient, cardShape)
            .padding(horizontal = 22.dp)
    ) {
        Image(
            painter = painterResource(id = parameters.iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = parameters.iconOffset.dp)
                .size(parameters.iconSize.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = parameters.mainText,
                maxLines = 2,
                modifier = Modifier.weight(parameters.weights.first, true),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                ),
            )
            Text(
                text = parameters.text,
                modifier = Modifier.weight(parameters.weights.second, true),
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
    parameters: TimeSpentCardParameters,
) = StatsCard(
    parameters = StatsCardParameters(
        gradient = parameters.gradient,
        roundedCornerPercent = parameters.roundedCornerPercent,
        iconId = parameters.iconId,
        mainText = parameters.mainText,
        text = "${parameters.time.hours}H, ${parameters.time.minutes}M",
        onClick = parameters.onClick
    ),
)

@Composable
fun TimeSpentChip() {
}

@Composable
fun OtherStatsCard(
    parameters: OtherStatsCardParameters,
) = StatsCard(
    parameters = StatsCardParameters(
        gradient = parameters.gradient,
        roundedCornerPercent = parameters.roundedCornerPercent,
        iconId = parameters.iconId,
        mainText = parameters.mainText,
        text = parameters.language,
        weights = Pair(1f, 0.5f),
        iconOffset = 90,
        iconSize = 70,
        onClick = parameters.onClick
    ),
)

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TimeSpentCardPreview() = WakaTimeAppTheme(darkTheme = true) {
    TimeSpentCard(
        parameters = TimeSpentCardParameters(
            Gradients.primary,
            25,
            R.drawable.ic_time,
            "Total Time Spent Today",
            Time(42, 22, 0f),
            onClick = {}
        )
    )
}
