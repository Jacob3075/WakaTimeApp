package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.ui.components.cards.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
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
    val icons = MaterialTheme.assets.icons
    val typography = MaterialTheme.typography
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Other Daily Stats", style = typography.sectionTitle)
        Text(text = "Details", color = colors.primary, style = typography.sectionSubtitle)
    }
    Spacer(modifier = Modifier.height(spacing.extraSmall))
    OtherStatsCard(
        gradient = gradients.greenCyan,
        iconId = icons.codeFile,
        mainText = "Most Language Used",
        language = dailyStats?.mostUsedLanguage.orEmpty(),
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.blueCyan,
        iconId = icons.laptop,
        mainText = "Most OS Used",
        language = dailyStats?.mostUsedOs.orEmpty(),
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.purpleCyanLight,
        iconId = icons.laptop,
        mainText = "Most Editor Used",
        language = dailyStats?.mostUsedEditor.orEmpty(),
        onClick = onClick
    )
}
