@file: Suppress("MagicNumber")

package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.components.VicoBarChart
import com.jacob.wakatimeapp.core.ui.components.VicoBarChartData
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

@Composable
internal fun TimeTab(today: LocalDate, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier.fillMaxSize(),
    ) {
        RecentTimeSpentChart(emptyMap<LocalDate, Time>().toImmutableMap(), today)
        QuickStatsCards()
        ProjectHistory()
    }
}

@Composable
private fun QuickStatsCards() {
    Text(text = "Quick Stats Cards")
}

@Composable
private fun ProjectHistory() {
    Text(text = "Project History")
}

@Composable
private fun RecentTimeSpentChart(weeklyTimeSpent: ImmutableMap<LocalDate, Time>, today: LocalDate) = Surface(
    modifier = Modifier
        .padding(horizontal = MaterialTheme.spacing.small)
        .padding(top = MaterialTheme.spacing.sMedium)
        .aspectRatio(ratio = 1.4f),
    shape = RoundedCornerShape(percent = 10),
    shadowElevation = 10.dp,
    tonalElevation = 2.dp,
) {
    VicoBarChart(
        timeData = weeklyTimeSpent.values.toMutableList().takeLast(30).toImmutableList(),
        xAxisFormatter = VicoBarChartData.getDefaultXAxisFormatter(today, skipCount = 5),
        modifier = Modifier.padding(MaterialTheme.spacing.small),
        columnWidth = 30f,
        showLabel = false
    )
}
