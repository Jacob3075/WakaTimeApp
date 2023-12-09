package com.jacob.wakatimeapp.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.jacob.wakatimeapp.core.common.utils.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.Time
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

@Composable
fun VicoBarChart(
    timeData: ImmutableCollection<Time>,
    xAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence,
    modifier: Modifier = Modifier,
    labelFormatter: ValueFormatter = VicoBarChart.defaultLabelFormatter,
    yAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence = VicoBarChart.defaultYAxisFormatter,
) {
    val entries = rememberChartEntries(timeData)
    val maxY = remember(entries) { entries.getModel()?.maxY?.ceil }

    Chart(
        modifier = modifier,
        chartModelProducer = entries,
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = MaterialTheme.colorScheme.primary.toArgb(),
                    thicknessDp = 10f,
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
private fun rememberChartEntries(timeData: ImmutableCollection<Time>) = remember(timeData) {
    timeData.mapIndexed { index, value ->
        entryOf(
            index.toFloat(),
            value.decimal,
        )
    }.let { ChartEntryModelProducer(it) }
}

object VicoBarChart {
    val defaultLabelFormatter = object : ValueFormatter {
        override fun formatValue(
            value: Float,
            chartValues: ChartValues,
        ) = Time.fromDecimal(value).formattedPrint()
    }
    val defaultYAxisFormatter = { value: Float, _: ChartValues -> "${value.toInt()}H" }

    fun getDefaultXAxisFormatter(today: LocalDate): (Float, ChartValues) -> String = { value, chartValues ->
        today.minus(
            chartValues.chartEntryModel.entries.size - value.toLong(),
            DateTimeUnit.DAY,
        ).getDisplayNameForDay()
    }
}
