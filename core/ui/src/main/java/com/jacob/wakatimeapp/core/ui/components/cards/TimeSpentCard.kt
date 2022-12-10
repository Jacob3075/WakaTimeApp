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

@Composable
fun TimeSpentCard(
    statsType: String,
    time: Time,
    gradient: Gradient,
    @DrawableRes iconId: Int,
    roundedCornerPercent: Int,
    onClick: () -> Unit,
) = StatsCard(
    statsType = statsType,
    statsValue = "${time.hours}H, ${time.minutes}M",
    gradient = gradient,
    iconId = iconId,
    onClick = onClick,
    roundedCornerPercent = roundedCornerPercent,
)

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
