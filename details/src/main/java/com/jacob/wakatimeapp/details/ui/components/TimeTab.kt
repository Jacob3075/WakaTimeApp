@file: Suppress("MagicNumber")

package com.jacob.wakatimeapp.details.ui.components

import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jacob.wakatimeapp.core.common.utils.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.components.RoundedBarChart
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

@Composable
internal fun TimeTab(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = modifier.fillMaxSize(),
    ) {
        RecentTimeSpentChart(emptyMap<LocalDate, Time>().toImmutableMap())
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
private fun RecentTimeSpentChart(weeklyTimeSpent: ImmutableMap<LocalDate, Time>) {
    val cardShape = RoundedCornerShape(percent = 10)
    val colorScheme = MaterialTheme.colorScheme

    val labels = rememberLabels(weeklyTimeSpent.keys)
    val barData = rememberBarData(weeklyTimeSpent.values, colorScheme)

    val spacing = MaterialTheme.spacing

    Surface(
        modifier = Modifier
            .padding(horizontal = spacing.small)
            .aspectRatio(1.4f),
        shape = cardShape,
        shadowElevation = 10.dp,
        tonalElevation = 2.dp,
    ) {
        val onSurface = MaterialTheme.colorScheme.onSurface.toArgb()
        AndroidView(
            modifier = Modifier.padding(spacing.small),
            factory = {
                RoundedBarChart(it).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT,
                    )
                    data = barData

                    configureChartProperties()
                    configureAxis(labels, onSurface)

                    invalidate()
                }
            },
            update = {
                it.data = barData
                it.xAxis.valueFormatter = XAxisDayFormatter(labels)
                it.invalidate()
            },
        )
    }
}

@Composable
private fun rememberLabels(days: ImmutableSet<LocalDate>): ImmutableMap<Int, String> = remember {
    days.mapIndexed { index, value ->
        index to value.getDisplayNameForDay()
    }.toMap()
        .toImmutableMap()
}

@Composable
private fun rememberBarData(weeklyStats: ImmutableCollection<Time>, colorScheme: ColorScheme) =
    remember(weeklyStats) {
        val onSurface = colorScheme.onSurface.toArgb()
        val barColor = colorScheme.primary.toArgb()

        val entries = weeklyStats.mapIndexed { index, value ->
            BarEntry(
                index.toFloat(),
                value.decimal,
                value,
            )
        }
        val barDataSet = BarDataSet(entries, "Label").apply {
            setDrawValues(true)
            isHighlightEnabled = false
            valueTextColor = onSurface
            valueFormatter = BarValueFormatter()
            color = barColor
        }
        BarData(barDataSet).apply { barWidth = 0.3f }
    }

private fun RoundedBarChart.configureAxis(labels: ImmutableMap<Int, String>, onSurface: Int) {
    xAxis.apply {
        setDrawGridLines(false)
        textColor = onSurface
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = XAxisDayFormatter(labels)
    }
    axisLeft.apply {
        setDrawGridLines(true)
        isEnabled = true
        spaceBottom = 0f
        labelCount = 3
        textColor = onSurface
        textSize = 8f
        valueFormatter = YAxisHourFormatter()
        axisMinimum = 0.1f // Can't be zero because of RoundedBarChart
    }
    axisRight.setDrawGridLines(false)
    axisRight.isEnabled = false
}

private fun RoundedBarChart.configureChartProperties() {
    setRadius(10)
    setScaleEnabled(false)
    setPinchZoom(false)
    isDoubleTapToZoomEnabled = false
    description.isEnabled = false
    legend.isEnabled = false
    animateY(3000, Easing.EaseInOutBack)
}

private class XAxisDayFormatter(private val labels: ImmutableMap<Int, String>) : ValueFormatter() {
    /**
     * Used to draw axis labels, calls [.getFormattedValue] by default.
     *
     * @param value float to be formatted
     * @param axis  axis being labeled
     * @return formatted string label
     */
    override fun getAxisLabel(value: Float, axis: AxisBase) = labels[value.toInt()] ?: "Nan"

    /**
     * Called when drawing any label, used to change numbers into formatted strings.
     *
     * @param value float to be formatted
     * @return formatted string label
     */
    override fun getFormattedValue(value: Float) = "%.1f".format(value)
}

private class YAxisHourFormatter : ValueFormatter() {
    /**
     * Used to draw axis labels, calls [.getFormattedValue] by default.
     *
     * @param value float to be formatted
     * @param axis  axis being labeled
     * @return formatted string label
     */
    override fun getAxisLabel(value: Float, axis: AxisBase) = "${value.toInt()}H"
}

private class BarValueFormatter : ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String =
        Time.fromDecimal(barEntry?.y ?: 0f)
            .formattedPrint()
}
