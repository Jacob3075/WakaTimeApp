package com.jacob.wakatimeapp.core.ui.components

import android.text.TextUtils.TruncateAt.MARQUEE
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.jacob.wakatimeapp.core.common.utils.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.core.DEF_LABEL_COUNT
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer.Horizontal
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer.Vertical
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
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
    labelFormatter: ValueFormatter = VicoBarChartData.defaultLabelFormatter,
    yAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence = VicoBarChartData.defaultYAxisFormatter,
    columnWidth: Float? = null,
    showLabel: Boolean = true,
) {
    val entries = rememberChartEntries(timeData)
    val maxY = remember(entries) { entries.getModel()?.maxY?.ceil }

    Chart(
        bottomAxis = rememberBottomAxis(
            label = axisLabelComponent(ellipsize = MARQUEE),
            valueFormatter = xAxisFormatter,
            guideline = null,
            itemPlacer = remember {
                Horizontal.default(
                    shiftExtremeTicks = false,
                    addExtremeLabelPadding = true,
                )
            },
        ),
        chart = createColumnChart(columnWidth, maxY, labelFormatter, showLabel),
        startAxis = createVerticalAxis(yAxisFormatter, maxY),
        modifier = modifier.padding(vertical = MaterialTheme.spacing.extraSmall),
        chartModelProducer = entries,
        isZoomEnabled = false,
        runInitialAnimation = true,
        chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
        horizontalLayout = HorizontalLayout.fullWidth(),
    )
}

@Composable
private fun createColumnChart(
    columnWidth: Float?,
    maxY: Float?,
    labelFormatter: ValueFormatter,
    showLabel: Boolean,
) = columnChart(
    columns = listOf(
        LineComponent(
            color = MaterialTheme.colorScheme.primary.toArgb(),
            thicknessDp = columnWidth ?: 2f,
            shape = Shapes.roundedCornerShape(topLeftPercent = 50, topRightPercent = 50),
        ),
    ),
    axisValuesOverrider = AxisValuesOverrider.fixed(minY = 0f, maxY = maxY),
    dataLabel = if (showLabel)
        textComponent {
            color = MaterialTheme.colorScheme.onSurface.toArgb()
            textSizeSp = MaterialTheme.typography.labelSmall.fontSize.value
            ellipsize = MARQUEE
        } else null,
    dataLabelValueFormatter = labelFormatter,
)

@Composable
private fun createVerticalAxis(
    yAxisFormatter: (value: Float, chartValues: ChartValues) -> CharSequence,
    maxY: Float?,
) = rememberStartAxis(
    valueFormatter = yAxisFormatter,
    itemPlacer = remember { Vertical.default(maxY?.toInt()?.inc() ?: DEF_LABEL_COUNT) },
)

@Composable
private fun rememberChartEntries(timeData: ImmutableCollection<Time>) = remember(timeData) {
    timeData.mapIndexed { index, value ->
        entryOf(
            index.toFloat(),
            value.decimal,
        )
    }.let { ChartEntryModelProducer(it) }
}

object VicoBarChartData {
    val defaultLabelFormatter = object : ValueFormatter {
        override fun formatValue(
            value: Float,
            chartValues: ChartValues,
        ) = when (val fromDecimal = Time.fromDecimal(value)) {
            Time.ZERO -> ""
            else -> fromDecimal.formattedPrint()
        }
    }
    val defaultYAxisFormatter = { value: Float, _: ChartValues -> "${value.toInt()}H" }

    fun getDefaultXAxisFormatter(today: LocalDate, skipCount: Int = 1): (Float, ChartValues) -> String = { value, chartValues ->
        if (value.toInt() % skipCount != 0) ""
        else
            today.minus(
                chartValues.chartEntryModel.entries.first().size - 1 - value.toLong(),
                DateTimeUnit.DAY,
            ).getDisplayNameForDay()
    }
}
