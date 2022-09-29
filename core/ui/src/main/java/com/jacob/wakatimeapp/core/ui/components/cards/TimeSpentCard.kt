package com.jacob.wakatimeapp.core.ui.components.cards

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.theme.Gradient
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.gradients

// TODO: FIX MAGIC NUMBERS

@Composable
fun TimeSpentCard(
    gradient: Gradient,
    roundedCornerPercent: Int,
    @DrawableRes iconId: Int,
    mainText: String,
    time: Time,
    onClick: () -> Unit,
) = StatsCard(
    gradient = gradient,
    roundedCornerPercent = roundedCornerPercent,
    iconId = iconId,
    mainText = mainText,
    text = "${time.hours}H, ${time.minutes}M",
    onClick = onClick
)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TimeSpentCardPreview() = WakaTimeAppTheme(darkTheme = true) {
    TimeSpentCard(
        gradient = MaterialTheme.gradients.primary,
        roundedCornerPercent = 25,
        iconId = drawable.ic_time,
        mainText = "Total Time Spent Today",
        time = Time(42, 22, 0f),
        onClick = {}
    )
}
