package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.components.cards.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.theme.Colors
import com.jacob.wakatimeapp.core.ui.theme.Gradients

@Composable
fun OtherDailyStatsSection(
    dailyStats: DailyStats?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
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
            iconId = drawable.ic_code_file,
            mainText = "Most Language Used",
            language = dailyStats?.mostUsedLanguage.orEmpty(),
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanLight,
            iconId = drawable.ic_laptop,
            mainText = "Most OS Used",
            language = dailyStats?.mostUsedOs.orEmpty(),
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            gradient = Gradients.purpleCyanLight,
            iconId = drawable.ic_laptop,
            mainText = "Most OS Used",
            language = dailyStats?.mostUsedOs.orEmpty(),
            onClick = onClick
        )
    }
}
