package com.jacob.wakatimeapp.home.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeeklyReportPreview() = WakaTimeAppTheme(darkTheme = true) {
    Surface {
        WeeklyReport()
    }
}
