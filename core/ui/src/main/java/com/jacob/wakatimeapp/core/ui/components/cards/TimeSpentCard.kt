package com.jacob.wakatimeapp.core.ui.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
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

@WtaPreviews
@Composable
private fun TimeSpentCardPreview() = WakaTimeAppTheme {
    Surface {
        Row {
            TimeSpentCard(
                gradient = MaterialTheme.gradients.facebookMessenger,
                roundedCornerPercent = 25,
                iconId = MaterialTheme.assets.icons.time,
                mainText = "Total Time Spent Today",
                time = Time(42, 22, 0f),
                onClick = {}
            )
        }
    }
}
