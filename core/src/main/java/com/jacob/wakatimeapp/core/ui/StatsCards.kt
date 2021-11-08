package com.jacob.wakatimeapp.core.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
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
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.Gradient
import com.jacob.wakatimeapp.core.ui.theme.Gradients
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.R

@Composable
fun StatsCard(
    gradient: Gradient,
    roundedCornerPercent: Int = 25,
    @DrawableRes iconId: Int,
    mainText: String,
    text: String,
    weights: Pair<Float, Float> = Pair(1f, 1f),
    iconOffset: Int = 50,
    iconSize: Int = 80,
) {
    val cardGradient =
        Brush.horizontalGradient(listOf(gradient.startColor, gradient.endColor))
    val cardShape = RoundedCornerShape(roundedCornerPercent)
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
            painter = painterResource(id = iconId),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(start = iconOffset.dp)
                .size(iconSize.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mainText,
                maxLines = 2,
                modifier = Modifier.weight(weights.first, true),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                ),
            )
            Text(
                text = text,
                modifier = Modifier.weight(weights.second, true),
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
    gradient: Gradient,
    roundedCornerPercent: Int,
    @DrawableRes iconId: Int,
    mainText: String,
    time: Time,
) = StatsCard(
    gradient = gradient,
    roundedCornerPercent = roundedCornerPercent,
    iconId = iconId,
    mainText = mainText,
    text = "${time.hours}H, ${time.minutes}M"
)

@Composable
fun TimeSpentChip() {
}

@Composable
fun OtherStatsCard(
    gradient: Gradient,
    roundedCornerPercent: Int,
    @DrawableRes iconId: Int,
    mainText: String,
    language: String,
) = StatsCard(
    gradient = gradient,
    roundedCornerPercent = roundedCornerPercent,
    iconId = iconId,
    mainText = mainText,
    text = language,
    weights = Pair(1f, 0.5f),
    iconOffset = 90,
    iconSize = 70
)

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun TimeSpentCardPreview() = WakaTimeAppTheme(darkTheme = true) {
    TimeSpentCard(
        Gradients.primary,
        25,
        R.drawable.ic_time,
        "Total Time Spent Today",
        Time(42, 22, 0f)
    )
}
