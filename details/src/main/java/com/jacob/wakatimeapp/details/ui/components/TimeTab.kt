package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.VicoBarChart
import com.jacob.wakatimeapp.core.ui.components.VicoBarChartData
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.ui.DetailsPageViewState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private const val DaysInChart = 30

@Composable
internal fun TimeTab(detailsPageData: DetailsPageViewState.Loaded, today: LocalDate, modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.spacing.sMedium),
    ) {
        item { RecentTimeSpentChart(detailsPageData, today) }
        item { QuickStatsCards(detailsPageData) }
        projectHistory(detailsPageData)
    }
}

@Composable
private fun RecentTimeSpentChart(detailsPageData: DetailsPageViewState.Loaded, today: LocalDate) = Surface(
    modifier = Modifier
        .padding(horizontal = MaterialTheme.spacing.small)
        .padding(top = MaterialTheme.spacing.sMedium)
        .aspectRatio(ratio = 1.4f),
    shape = RoundedCornerShape(percent = 10),
    shadowElevation = 10.dp,
    tonalElevation = 2.dp,
) {
    VicoBarChart(
        timeData = detailsPageData.statsForProject.values.toMutableList().takeLast(DaysInChart).toImmutableList(),
        xAxisFormatter = VicoBarChartData.getDefaultXAxisFormatter(today, skipCount = 5),
        modifier = Modifier.padding(MaterialTheme.spacing.small),
        columnWidth = 30f,
        showLabel = false,
    )
}

@WtaPreviews
@Composable
private fun ProjectHistoryListPreview() {
    WakaTimeAppTheme {
        Surface {
            TimeTab(
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
                today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            )
        }
    }
}
