package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.R.drawable
import com.jacob.wakatimeapp.common.models.Time
import com.jacob.wakatimeapp.common.ui.OtherStatsCard
import com.jacob.wakatimeapp.common.ui.TimeSpentCard
import com.jacob.wakatimeapp.common.ui.theme.Colors
import com.jacob.wakatimeapp.common.ui.theme.Gradients
import com.jacob.wakatimeapp.home.domain.models.DailyStats

@Composable
fun TimeSpentSection(dailyStats: DailyStats?, onDailyTimeSpentCardClick: () -> Unit) = TimeSpentCard(
    gradient = Gradients.primary,
    roundedCornerPercent = 25,
    iconId = drawable.ic_time,
    mainText = "Total Time Spent Today",
    time = dailyStats?.timeSpent ?: Time(0, 0, 0f),
    onClick = onDailyTimeSpentCardClick
)

@Composable
fun OtherDailyStats(dailyStats: DailyStats?) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Other Daily Stats", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "Details", color = Colors.AccentText, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.greenCyan,
            roundedCornerPercent = 25,
            iconId = drawable.ic_code_file,
            mainText = "Most Language Used",
            language = dailyStats?.mostUsedLanguage ?: ""
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanLight,
            roundedCornerPercent = 25,
            iconId = drawable.ic_laptop,
            mainText = "Most OS Used",
            language = dailyStats?.mostUsedOs ?: ""
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanDark,
            roundedCornerPercent = 25,
            iconId = drawable.ic_code,
            mainText = "Most Editor Used",
            language = dailyStats?.mostUsedEditor ?: ""
        )
    }
}
