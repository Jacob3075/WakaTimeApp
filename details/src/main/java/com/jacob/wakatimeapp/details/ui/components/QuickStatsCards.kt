package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
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
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.StatsChip
import com.jacob.wakatimeapp.core.ui.modifiers.removeFontPadding
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
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
        // TODO: REPLACE CHIP WITH CARD
        StatsChip(
            gradient = MaterialTheme.gradients.amin,
            iconId = MaterialTheme.assets.icons.time,
            roundedCornerPercent = 15,
        ) {
            ChipContent(
                statsType = "Total Time on Project",
                statsValue = detailsPageData.totalTime.formattedPrint(),
                statsValueSubText = "",
                gradient = MaterialTheme.gradients.amin,
            )
        }

        StatsChip(
            gradient = MaterialTheme.gradients.purpink,
            iconId = MaterialTheme.assets.icons.time,
            roundedCornerPercent = 15,
        ) {
            ChipContent(
                statsType = "Average Time",
                statsValue = detailsPageData.averageTime.formattedPrint(),
                statsValueSubText = "",
                gradient = MaterialTheme.gradients.purpink,
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            StatsChip(
                gradient = MaterialTheme.gradients.quepal,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    statsType = "Start date",
                    statsValue = detailsPageData.startDate.format(format),
                    statsValueSubText = "",
                    gradient = MaterialTheme.gradients.quepal,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

            StatsChip(
                gradient = MaterialTheme.gradients.tealLove,
                iconId = MaterialTheme.assets.icons.time,
                roundedCornerPercent = 15,
                modifier = Modifier.weight(1f),
            ) {
                ChipContent(
                    statsType = "No. of days worked",
                    statsValue = detailsPageData.numberOfDaysWorked.toString(),
                    statsValueSubText = "",
                    gradient = MaterialTheme.gradients.tealLove,
                    statValueTextStyle = MaterialTheme.typography.titleMedium,
                )
            }
        }

        // TODO: ADD PENDING CARDS/CHIPS TO UI

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
