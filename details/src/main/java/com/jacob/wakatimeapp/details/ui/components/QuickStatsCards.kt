package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.FlippableStatsCard
import com.jacob.wakatimeapp.core.ui.components.cards.FlippableStatsChip
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
        FlippableStatsCard(
            gradient = MaterialTheme.gradients.purpink,
            roundedCornerPercent = 15,
            iconId = MaterialTheme.assets.icons.time,
            frontContent = {
                CardContent(
                    cardSubHeading = "Total Time on Project",
                    cardHeading = detailsPageData.totalTime.formattedPrint(),
                    gradient = MaterialTheme.gradients.purpink,
                )
            },
            backContent = {
                CardContent(
                    cardSubHeading = "Average Time",
                    cardHeading = detailsPageData.averageTime.formattedPrint(),
                    gradient = MaterialTheme.gradients.purpink,
                )
            },
        )

        ProjectStreakChips(detailsPageData)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            FlippableStatsChip(
                modifier = Modifier.weight(1f),
                gradient = MaterialTheme.gradients.quepal,
                iconId = MaterialTheme.assets.icons.calendar,
                frontContent = {
                    ChipContent(
                        cardSubHeading = "Start date",
                        cardHeading = detailsPageData.startDate.format(format),
                        gradient = MaterialTheme.gradients.quepal,
                        statValueTextStyle = MaterialTheme.typography.titleLarge,
                    )
                },
                backContent = {
                    ChipContent(
                        cardSubHeading = "No. of days worked",
                        cardHeading = detailsPageData.numberOfDaysWorked.toString(),
                        gradient = MaterialTheme.gradients.quepal,
                        statValueTextStyle = MaterialTheme.typography.headlineSmall,
                    )
                },
            )
            FlippableStatsChip(
                modifier = Modifier.weight(1f),
                gradient = MaterialTheme.gradients.flare,
                iconId = MaterialTheme.assets.icons.calendar,
                frontContent = {
                    ChipContent(
                        cardHeading = detailsPageData.dayMostWorked.format(format),
                        cardSubHeading = "Day Most Worked",
                        gradient = MaterialTheme.gradients.flare,
                        statValueTextStyle = MaterialTheme.typography.titleLarge,
                    )
                },
                backContent = {
                    ChipContent(
                        cardHeading = detailsPageData.maxTimeWorkedInDay.formattedPrint(),
                        cardSubHeading = "Most Time in 1 Day",
                        gradient = MaterialTheme.gradients.flare,
                        statValueTextStyle = MaterialTheme.typography.headlineSmall,
                    )
                },
            )
        }

        SecondaryStatsCards(detailsPageData.uiData)
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
