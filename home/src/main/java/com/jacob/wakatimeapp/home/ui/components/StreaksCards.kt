package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.StatsChip
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.home.domain.models.Streak
import kotlinx.datetime.LocalDate

@Composable
internal fun CurrentStreakCard(
    currentStreak: Streak,
    gradient: Gradient,
    roundedCornerPercent: Int,
    modifier: Modifier = Modifier,
) {
    StatsChip(
        statsType = "Current Streak",
        statsValue = "${currentStreak.days}",
        statsValueSubText = "days",
        gradient = gradient,
        iconId = MaterialTheme.assets.icons.time,
        onClick = {},
        roundedCornerPercent = roundedCornerPercent,
        modifier = modifier,
    )
}

@Composable
internal fun LongestStreakCard(modifier: Modifier = Modifier): Unit = TODO(modifier.toString())

@Composable
internal fun DaysWorkedInWeek(
    numberOfDaysWorked: Int,
    gradient: Gradient,
    roundedCornerPercent: Int,
    modifier: Modifier = Modifier,
) {
    StatsChip(
        statsType = "Current Streak",
        statsValue = "$numberOfDaysWorked",
        statsValueSubText = "/7 days",
        gradient = gradient,
        iconId = MaterialTheme.assets.icons.time,
        onClick = {},
        roundedCornerPercent = roundedCornerPercent,
        modifier = modifier,
    )
}

@WtaPreviews
@Composable
private fun CurrentStreakCardPreview() = WakaTimeAppTheme {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CurrentStreakCard(
                currentStreak = Streak(
                    start = LocalDate(2022, 1, 1),
                    end = LocalDate(2022, 1, 11),
                ),
                gradient = MaterialTheme.gradients.purpink,
                roundedCornerPercent = 20,
                modifier = Modifier.weight(0.5F),
            )
            DaysWorkedInWeek(
                numberOfDaysWorked = 5,
                gradient = MaterialTheme.gradients.tealLove,
                roundedCornerPercent = 20,
                modifier = Modifier.weight(0.5F),
            )
        }
    }
}
