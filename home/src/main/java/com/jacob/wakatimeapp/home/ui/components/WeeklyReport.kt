package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.ui.theme.Colors
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import java.time.format.TextStyle.SHORT
import java.util.*


@Composable
fun WeeklyReport(parameters: WeeklyReportParameters) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Weekly Report", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Details", color = Colors.AccentText, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyReportChart(parameters.dailyStats ?: emptyList())
    }
}

@Composable
private fun WeeklyReportChart(dailyStats: List<DailyStats>) {
    val cardShape = RoundedCornerShape(10)

    val labels = mutableMapOf<Int, String>()
    val entries = dailyStats.mapIndexed { index, value ->
        labels[index] = value.date.dayOfWeek.getDisplayName(SHORT, Locale.getDefault())
        BarEntry(
            index.toFloat(),
            value.timeSpent.decimal,
            value
        )
    }

    val dataSet = BarDataSet(entries, "Label").apply {
        setDrawValues(false)
        valueTextColor = Color.WHITE
    }
    val barData = BarData(dataSet).apply { barWidth = 0.3f }

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .shadow(elevation = 8.dp, shape = cardShape)
            .aspectRatio(1.4f)
            .background(Colors.CardBGPrimary, shape = cardShape)
    ) {
        AndroidView(
            modifier = Modifier.padding(8.dp),
            factory = {
                RoundedBarChart(it).apply {
                    setRadius(10)

                    setScaleEnabled(false)
                    setPinchZoom(false)
                    isDoubleTapToZoomEnabled = false
                    description.isEnabled = false

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

                    legend.isEnabled = false

                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    data = barData
                    invalidate()

                    animateY(3000, Easing.EaseInOutBack)
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

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeeklyReportPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        WeeklyReport(WeeklyReportParameters(emptyList()))
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
