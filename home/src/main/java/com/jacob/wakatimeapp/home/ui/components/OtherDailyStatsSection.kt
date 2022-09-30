package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.ui.R.drawable
import com.jacob.wakatimeapp.core.ui.components.cards.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun OtherDailyStatsSection(
    dailyStats: DailyStats?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.fillMaxWidth()
) {
    val gradients = MaterialTheme.gradients
    val spacing = MaterialTheme.spacing
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Other Daily Stats", fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "Details", color = colors.primary, fontSize = 14.sp)
    }
    Spacer(modifier = Modifier.height(spacing.medium))
    OtherStatsCard(
        gradient = gradients.greenCyan,
        iconId = drawable.ic_code_file,
        mainText = "Most Language Used",
        language = dailyStats?.mostUsedLanguage.orEmpty(),
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.blueCyan,
        iconId = drawable.ic_laptop,
        mainText = "Most OS Used",
        language = dailyStats?.mostUsedOs.orEmpty(),
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.purpleCyanLight,
        iconId = drawable.ic_laptop,
        mainText = "Most Editor Used",
        language = dailyStats?.mostUsedEditor.orEmpty(),
        onClick = onClick
    )
}
