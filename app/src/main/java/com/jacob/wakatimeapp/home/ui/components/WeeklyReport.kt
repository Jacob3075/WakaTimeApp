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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.ui.theme.Colors
import com.jacob.wakatimeapp.common.ui.theme.WakaTimeAppTheme

@Composable
fun WeeklyReport() {
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
        WeeklyReportChart()
    }
}

@Composable
private fun WeeklyReportChart() {
    val cardShape = RoundedCornerShape(10)
    Box(
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = cardShape)
            .aspectRatio(1.4f)
            .padding(10.dp)
            .background(Colors.CardBGPrimary, shape = cardShape)
    ) {
        AndroidView(
            modifier = Modifier.padding(8.dp),
            factory = { context ->
                val entries = listOf(
                    BarEntry(1f, 1f),
                    BarEntry(2f, 2f),
                    BarEntry(3f, 3f),
                    BarEntry(4f, 4f),
                    BarEntry(5f, 5f),
                    BarEntry(6f, 6f),
                    BarEntry(7f, 7f),
                )
                val dataSet = BarDataSet(entries, "Label").also { it.setDrawValues(false) }
                val barData = BarData(dataSet).also { it.barWidth = 0.3f }

                BarChart(context).apply {
                    setScaleEnabled(false)
                    setPinchZoom(false)
                    isDoubleTapToZoomEnabled = false
                    description.isEnabled = false

                    xAxis.apply {
                        setDrawGridLines(false)
                        textColor = Color.WHITE
                        position = BOTTOM
                        valueFormatter = XAxisDayFormatter()
                    }
                    axisLeft.setDrawGridLines(false)
                    axisLeft.isEnabled = false
                    axisRight.setDrawGridLines(false)
                    axisRight.isEnabled = false

                    legend.isEnabled = false

                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    data = barData
                    invalidate()
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeeklyReportPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        WeeklyReport()
    }
}

private class XAxisDayFormatter() : ValueFormatter() {
    /**
     * Used to draw bar labels, calls [.getFormattedValue] by default.
     *
     * @param barEntry bar being labeled
     * @return formatted string label
     */
    override fun getBarLabel(barEntry: BarEntry): String {
        return super.getBarLabel(barEntry)
    }
}
