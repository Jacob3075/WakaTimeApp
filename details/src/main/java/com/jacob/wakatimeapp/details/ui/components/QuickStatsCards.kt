package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.StatsChip
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.ui.DetailsPageViewState
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

@Composable
internal fun QuickStatsCards(detailsPageData: DetailsPageViewState.Loaded) {
    val totalTime = remember(detailsPageData) { detailsPageData.statsForProject.values.fold(Time.ZERO, Time::plus) }
    val averageTime =
        remember(detailsPageData) { Time.fromDecimal(totalTime.decimal.div(detailsPageData.statsForProject.size)) }
    val startDate = remember(detailsPageData) { detailsPageData.statsForProject.keys.minBy(LocalDate::toEpochDays) }
    val numberOfDaysWorked =
        remember(detailsPageData) { detailsPageData.statsForProject.values.filter { it != Time.ZERO }.size }

    Column(
        Modifier.fillMaxWidth(),
        Arrangement.spacedBy(MaterialTheme.spacing.sMedium),
    ) {
        StatsChip(
            statsType = "Total Time on Project",
            statsValue = totalTime.formattedPrint(),
            statsValueSubText = "",
            gradient = MaterialTheme.gradients.amin,
            iconId = MaterialTheme.assets.icons.time,
            onClick = {},
            roundedCornerPercent = 15,
        )
        StatsChip(
            statsType = "Average Time",
            statsValue = averageTime.formattedPrint(),
            statsValueSubText = "",
            gradient = MaterialTheme.gradients.purpink,
            iconId = MaterialTheme.assets.icons.time,
            onClick = {},
            roundedCornerPercent = 15,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            val format = LocalDate.Format {
                dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
                chars(", ")
                monthName(MonthNames.ENGLISH_ABBREVIATED)
                char(' ')
                dayOfMonth()
                chars(", ")
                yearTwoDigits(1960)
            }
            StatsChip(
                statsType = "Start date",
                statsValue = startDate.format(format),
                statsValueSubText = "",
                gradient = MaterialTheme.gradients.quepal,
                iconId = MaterialTheme.assets.icons.time,
                onClick = {},
                roundedCornerPercent = 15,
                statValueTextStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            StatsChip(
                statsType = "No. of days worked",
                statsValue = numberOfDaysWorked.toString(),
                statsValueSubText = "",
                gradient = MaterialTheme.gradients.tealLove,
                iconId = MaterialTheme.assets.icons.time,
                onClick = {},
                roundedCornerPercent = 15,
                statValueTextStyle = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            // expandable cards
            // could replace tabs?
            // when clicking on one of the cards will expand that card and shrink the other
            // will expand in both X and Y, show pie chart after expanding
            // before clicking/expanding
            // 🟥 🟦
            // 🟧 🟪
            // after clicking/expanding
            // 🟥🟥
            // 🟥🟥
            Text("Most used language")
            Text("Most used editor")
            Text("Most used os")
            Text("Most used machine")
        }
    }
}

@WtaPreviews
@Composable
private fun ProjectHistoryListPreview() {
    WakaTimeAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            QuickStatsCards(
                DetailsPageViewState.Loaded(
                    "",
                    mapOf(
                        LocalDate.fromEpochDays(1) to Time.fromDecimal(3f),
                        LocalDate.fromEpochDays(2) to Time.fromDecimal(1f),
                        LocalDate.fromEpochDays(3) to Time.fromDecimal(2f),
                        LocalDate.fromEpochDays(4) to Time.fromDecimal(2f),
                        LocalDate.fromEpochDays(5) to Time.fromDecimal(4f),
                        LocalDate.fromEpochDays(6) to Time.fromDecimal(3f),
                        LocalDate.fromEpochDays(7) to Time.fromDecimal(2f),
                        LocalDate.fromEpochDays(8) to Time.fromDecimal(3f),
                        LocalDate.fromEpochDays(9) to Time.fromDecimal(4f),
                        LocalDate.fromEpochDays(10) to Time.fromDecimal(1f),
                    ).toImmutableMap(),
                ),
            )
        }
    }
}
