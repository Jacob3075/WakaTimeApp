@file: Suppress("MagicNumber")

package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jacob.wakatimeapp.core.common.getDisplayNameForDay
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun WeeklyReport(
    dailyStats: List<DailyStats>?,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sMedium),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Weekly Report", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "Details", color = MaterialTheme.colors.primary, fontSize = 14.sp)
    }
    WeeklyReportChart(dailyStats.orEmpty())
}

@Composable
private fun WeeklyReportChart(dailyStats: List<DailyStats>) {
    val cardShape = RoundedCornerShape(percent = 10)

    val pairList by remember {
        derivedStateOf {
            dailyStats.mapIndexed { index, value -> index to value }
        }
    }

    val labels by getLabels(pairList)
    val barData by getBarData(pairList)

    val spacing = MaterialTheme.spacing

    Box(
        modifier = Modifier
            .padding(horizontal = spacing.small)
            .shadow(elevation = 8.dp, shape = cardShape, clip = false)
            .clip(shape = cardShape)
            .background(MaterialTheme.colors.surface, shape = cardShape)
            .aspectRatio(1.4f)
    ) {
        AndroidView(
            modifier = Modifier.padding(spacing.small),
            factory = {
                RoundedBarChart(it).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

                    configureChartProperties()
                    configureAxis(labels)

                    data = barData
                    invalidate()
                }
            },
            update = {
                it.data = barData
                it.xAxis.valueFormatter = XAxisDayFormatter(labels)
                it.invalidate()
            }
        )
    }
}

@Composable
private fun getLabels(pairList: List<Pair<Int, DailyStats>>) =
    remember {
        derivedStateOf {
            pairList.associate { (index, value) ->
                index to value.date.getDisplayNameForDay()
            }
        }
    }

@Composable
private fun getBarData(pairList: List<Pair<Int, DailyStats>>) =
    remember {
        derivedStateOf {
            val entries = pairList.map { (index, value) ->
                BarEntry(
                    index.toFloat(),
                    value.timeSpent.decimal,
                    value
                )
            }
            val barDataSet = BarDataSet(entries, "Label").apply {
                setDrawValues(false)
                valueTextColor = Color.WHITE
            }
            BarData(barDataSet).apply { barWidth = 0.3f }
        }
    }

private fun RoundedBarChart.configureAxis(labels: Map<Int, String>) {
    xAxis.apply {
        setDrawGridLines(false)
        textColor = Color.WHITE
        position = BOTTOM
        valueFormatter = XAxisDayFormatter(labels)
    }
    axisLeft.apply {
        setDrawGridLines(true)
        isEnabled = true
        spaceBottom = 0f
        labelCount = 3
        textColor = Color.WHITE
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
        WeeklyReport(emptyList())
    }
}

private class XAxisDayFormatter(private val labels: Map<Int, String>) : ValueFormatter() {
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
