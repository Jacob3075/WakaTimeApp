@file: Suppress("MagicNumber")

package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
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
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jacob.wakatimeapp.core.common.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import kotlinx.collections.immutable.ImmutableCollection
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

@Composable
fun WeeklyReport(
    weeklyTimeSpent: ImmutableMap<LocalDate, Time>,
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
    WeeklyReportChart(weeklyTimeSpent)
}

@Composable
private fun WeeklyReportChart(weeklyTimeSpent: ImmutableMap<LocalDate, Time>) {
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
                    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
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
    remember(key1 = weeklyStats) {
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
        position = BOTTOM
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

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeeklyReportPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        WeeklyReport(emptyMap<LocalDate, Time>().toImmutableMap())
    }
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
