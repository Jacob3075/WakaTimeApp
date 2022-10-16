package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jacob.wakatimeapp.core.ui.components.cards.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing

@Composable
fun OtherDailyStatsSection(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mostUsedLanguage: String,
    mostUsedOs: String,
    mostUsedEditor: String,
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
        Text(text = "Details", color = colorScheme.primary, style = typography.sectionSubtitle)
    }
    Spacer(modifier = Modifier.height(spacing.extraSmall))
    OtherStatsCard(
        gradient = gradients.greenCyan,
        iconId = icons.codeFile,
        mainText = "Most Language Used",
        language = mostUsedLanguage,
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.purpleCyan,
        iconId = icons.laptop,
        mainText = "Most OS Used",
        language = mostUsedOs,
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        gradient = gradients.pinkCyanLight,
        iconId = icons.code,
        mainText = "Most Used Editor ",
        language = mostUsedEditor,
        onClick = onClick
    )
}
