package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.OtherStatsCardParameters
import com.jacob.wakatimeapp.core.ui.theme.Colors
import com.jacob.wakatimeapp.core.ui.theme.Gradients

@Composable
fun OtherDailyStatsSection(parameters: OtherDailyStatsSectionParameters) {
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
            parameters = OtherStatsCardParameters(
                gradient = Gradients.greenCyan,
                roundedCornerPercent = 25,
                iconId = drawable.ic_code_file,
                mainText = "Most Language Used",
                language = parameters.dailyStats?.mostUsedLanguage ?: "",
                onClick = parameters.onClick
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            parameters = OtherStatsCardParameters(
                gradient = Gradients.purpleCyanLight,
                roundedCornerPercent = 25,
                iconId = drawable.ic_laptop,
                mainText = "Most OS Used",
                language = parameters.dailyStats?.mostUsedOs ?: "",
                onClick = parameters.onClick
            ),
        )
        Spacer(modifier = Modifier.height(15.dp))
        OtherStatsCard(
            parameters = OtherStatsCardParameters(
                gradient = Gradients.purpleCyanLight,
                roundedCornerPercent = 25,
                iconId = drawable.ic_laptop,
                mainText = "Most OS Used",
                language = parameters.dailyStats?.mostUsedOs ?: "",
                onClick = parameters.onClick
            ),
        )
    }
}
