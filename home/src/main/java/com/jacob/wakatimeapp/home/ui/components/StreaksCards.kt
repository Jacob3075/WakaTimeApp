package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.StatsChip
import com.jacob.wakatimeapp.core.ui.modifiers.removeFontPadding
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.models.Streak
import kotlinx.datetime.LocalDate

@Composable
internal fun StreakCard(
    text: String,
    streak: Streak,
    gradient: Gradient,
    roundedCornerPercent: Int,
    modifier: Modifier = Modifier,
) {
    StatsChip(
        gradient = gradient,
        iconId = MaterialTheme.assets.icons.time,
        roundedCornerPercent = roundedCornerPercent,
        modifier = modifier,
    ) {
        ChipContent(
            statsType = text,
            statsValue = "${if (streak == Streak.ZERO) 0 else streak.days}",
            statsValueSubText = "days",
            gradient = gradient,
        )
    }
}

// TODO: WHY SEPARATE CARD FOR THIS?
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
        gradient = gradient,
        iconId = MaterialTheme.assets.icons.time,
        roundedCornerPercent = roundedCornerPercent,
        modifier = modifier,
    ) {
        ChipContent(
            statsType = "Days Worked in Week",
            statsValue = "$numberOfDaysWorked",
            statsValueSubText = "/7 days",
            gradient = gradient,
        )
    }
}

@Composable
private fun ColumnScope.ChipContent(
    statsType: String,
    statsValue: String,
    statsValueSubText: String,
    gradient: Gradient,
    statValueTextStyle: TextStyle = MaterialTheme.typography.displayLarge,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = statValueTextStyle.toSpanStyle()) {
                append(statsValue)
            }
            withStyle(
                style = MaterialTheme.typography.headlineSmall.copy(
                    baselineShift = BaselineShift(multiplier = 0.5f),
                ).toSpanStyle(),
            ) {
                append(" $statsValueSubText")
            }
        },
        color = gradient.onStartColor,
        modifier = Modifier.removeFontPadding(statValueTextStyle),
    )
    Text(
        text = statsType,
        color = gradient.onStartColor,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
    )
}

@WtaPreviews
@Composable
private fun CurrentStreakCardPreview() = WakaTimeAppTheme {
    Surface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            StreakCard(
                text = "Current Streak",
                streak = Streak(
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
