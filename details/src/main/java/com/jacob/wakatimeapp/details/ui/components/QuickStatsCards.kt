package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.StatsCard
import com.jacob.wakatimeapp.core.ui.components.cards.StatsChip
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import com.jacob.wakatimeapp.details.ui.DetailsPageViewState
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

private const val EpochYear = 1970

private val format = LocalDate.Format {
    dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    chars(", ")
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    dayOfMonth()
    chars(", ")
    yearTwoDigits(EpochYear)
}

@Composable
internal fun QuickStatsCards(detailsPageData: DetailsPageViewState.Loaded) {
    Column(
        Modifier.fillMaxWidth(),
        Arrangement.spacedBy(MaterialTheme.spacing.sMedium),
    ) {
        StatsCard(
            gradient = MaterialTheme.gradients.amin,
            roundedCornerPercent = 15,
            iconId = MaterialTheme.assets.icons.time,
            onClick = {},
        ) {
            CardContent(
                cardSubHeading = "Total Time on Project",
                cardHeading = detailsPageData.totalTime.formattedPrint(),
                gradient = MaterialTheme.gradients.amin,
            )
        }

        StatsCard(
            gradient = MaterialTheme.gradients.purpink,
            roundedCornerPercent = 15,
            iconId = MaterialTheme.assets.icons.time,
            onClick = {},
        ) {
            CardContent(
                cardSubHeading = "Average Time",
                cardHeading = detailsPageData.averageTime.formattedPrint(),
                gradient = MaterialTheme.gradients.purpink,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            StatsChip(
                gradient = MaterialTheme.gradients.quepal,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "Start date",
                    cardHeading = detailsPageData.startDate.format(format),
                    gradient = MaterialTheme.gradients.quepal,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }

            StatsChip(
                gradient = MaterialTheme.gradients.quepal,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "No. of days worked",
                    cardHeading = detailsPageData.numberOfDaysWorked.toString(),
                    gradient = MaterialTheme.gradients.quepal,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }
        }

        // TODO: MERGE BELOW 2 ROWS INTO 1 AND MAKE THEN INTERACTABLE, CHIPS SWAP WHEN CLICKED
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            StatsChip(
                gradient = MaterialTheme.gradients.shifter,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "Longest Streak",
                    cardHeading = "${detailsPageData.longestStreakInProject.days} Days",
                    gradient = MaterialTheme.gradients.shifter,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }

            StatsChip(
                gradient = MaterialTheme.gradients.shifter,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "Current Streak",
                    cardHeading = "${detailsPageData.currentStreakInProject.days} Days",
                    gradient = MaterialTheme.gradients.shifter,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            StatsChip(
                gradient = MaterialTheme.gradients.flare,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "Day Most Worked",
                    cardHeading = detailsPageData.dayMostWorked.format(format),
                    gradient = MaterialTheme.gradients.flare,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }

            StatsChip(
                gradient = MaterialTheme.gradients.flare,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    cardSubHeading = "Most Time in 1 Day",
                    cardHeading = detailsPageData.maxTimeWorkedInDay.formattedPrint(),
                    gradient = MaterialTheme.gradients.flare,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }
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
                    uiData = DetailedProjectStatsUiData(
                        totalTime = Time.ZERO,
                        averageTime = Time.ZERO,
                        dailyProjectStats = mapOf(
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
                        languages = Languages.NONE,
                        operatingSystems = OperatingSystems.NONE,
                        editors = Editors.NONE,
                        machines = Machines.NONE,
                    ),
                    todaysDate = LocalDate.fromEpochDays(1),
                ),
            )
        }
    }
}
