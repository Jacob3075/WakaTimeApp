package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.components.WtaSurface
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.cardHeader
import com.jacob.wakatimeapp.core.ui.theme.cardSubtitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

@Composable
internal fun ProjectHistory(
    statsForProject: ImmutableMap<LocalDate, Time>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Text(text = "Project History", modifier = Modifier.padding(vertical = MaterialTheme.spacing.small))
        statsForProject
            .filter { it.value != Time.ZERO }
            .forEach { localDateTimePair ->
                ProjectHistoryItem(entry = localDateTimePair.toPair())
            }
    }
}

@Composable
fun ProjectHistoryItem(
    entry: Pair<LocalDate, Time>,
    modifier: Modifier = Modifier,
) {
    val format =
        LocalDate.Format {
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
            chars(", ")
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            dayOfMonth()
        }

    WtaSurface(modifier = modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.padding(MaterialTheme.spacing.small),
        ) {
            Text(
                text = entry.second.longFormattedPrint(),
                style = MaterialTheme.typography.cardHeader,
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            )
            Text(
                text = entry.first.format(format),
                style = MaterialTheme.typography.cardSubtitle,
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
            )
        }
    }
}

@WtaComponentPreviews
@Composable
private fun ProjectHistoryItemPreview() {
    WakaTimeAppTheme {
        Surface {
            ProjectHistoryItem(LocalDate.fromEpochDays(1) to Time(1, 1, 1f))
        }
    }
}

@WtaComponentPreviews
@Composable
private fun ProjectHistoryListPreview() {
    WakaTimeAppTheme {
        Surface {
            ProjectHistory(
                statsForProject =
                    mapOf(
                        LocalDate.fromEpochDays(1) to Time(1, 1, 1f),
                        LocalDate.fromEpochDays(2) to Time(1, 1, 1f),
                        LocalDate.fromEpochDays(3) to Time(1, 1, 1f),
                    ).toImmutableMap(),
            )
        }
    }
}
