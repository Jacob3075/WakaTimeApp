package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.common.utils.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.DEF_LABEL_COUNT
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.extension.ceil
import com.patrykandpatrick.vico.core.formatter.ValueFormatter
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@Composable
internal fun WeeklyReport(
    weeklyTimeSpent: ImmutableMap<LocalDate, Time>,
    today: LocalDate,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val typography = MaterialTheme.typography
        Text(text = "Weekly Report", style = typography.sectionTitle)
        Text(
            text = "Details",
            color = MaterialTheme.colorScheme.primary,
            style = typography.sectionSubtitle,
        )
    }
    WeeklyReportChart(weeklyTimeSpent, today)
}

@Composable
private fun WeeklyReportChart(weeklyTimeSpent: ImmutableMap<LocalDate, Time>, today: LocalDate) = Surface(
    modifier = Modifier
        .padding(horizontal = MaterialTheme.spacing.small)
        .aspectRatio(ratio = 1.4f),
    shape = RoundedCornerShape(percent = 10),
    shadowElevation = 10.dp,
    tonalElevation = 2.dp,
) {
    VicoBarChart(
        modifier = Modifier.padding(MaterialTheme.spacing.small),
        entries = rememberChartEntries(weeklyStats = weeklyTimeSpent.values),
        labelFormatter = object : ValueFormatter {
            override fun formatValue(
                value: Float,
                chartValues: ChartValues,
            ) = Time.fromDecimal(value).formattedPrint()
        },
        yAxisFormatter = { value, _ -> "${value.toInt()}H" },
        xAxisFormatter = { value, chartValues ->
            today.minus(
                chartValues.chartEntryModel.entries.size - value.toLong(),
                DateTimeUnit.DAY,
            ).getDisplayNameForDay()
        },
    )
}

@Composable
fun VicoBarChart(
    entries: ChartEntryModelProducer,
    labelFormatter: ValueFormatter,
    yAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence,
    xAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence,
    modifier: Modifier = Modifier,
) {
    val maxY = remember(entries) { entries.getModel()?.maxY?.ceil }

    Chart(
        modifier = modifier,
        chartModelProducer = entries,
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = MaterialTheme.colorScheme.primary.toArgb(),
                    thicknessDp = MaterialTheme.spacing.small.value,
                    shape = Shapes.roundedCornerShape(topLeftPercent = 50, topRightPercent = 50),
                ),
            ),
            axisValuesOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = maxY),
            dataLabel = textComponent {
                color = MaterialTheme.colorScheme.onSurface.toArgb()
                textSizeSp = MaterialTheme.typography.labelSmall.fontSize.value

            },
            dataLabelValueFormatter = labelFormatter,
        ),
        startAxis = rememberStartAxis(
            valueFormatter = yAxisFormatter,
            itemPlacer = remember { AxisItemPlacer.Vertical.default(maxY?.toInt()?.inc() ?: DEF_LABEL_COUNT) },
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = xAxisFormatter,
            guideline = null,
        ),
        isZoomEnabled = false,
        runInitialAnimation = true,
    )
}

@Composable
private fun rememberChartEntries(weeklyStats: ImmutableCollection<Time>) = remember(weeklyStats) {
    weeklyStats.mapIndexed { index, value ->
        entryOf(
            index.toFloat(),
            value.decimal,
        )
    }.let { ChartEntryModelProducer(it) }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeeklyReportPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        WeeklyReport(emptyMap<LocalDate, Time>().toImmutableMap(), LocalDate(2023, 1, 1))
    }
}
