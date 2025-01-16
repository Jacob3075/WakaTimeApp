package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jacob.wakatimeapp.core.models.Streak
import com.jacob.wakatimeapp.core.ui.components.cards.FlippableStatsChip
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.ui.DetailsPageViewState

@Composable
internal fun ProjectStreakChips(detailsPageData: DetailsPageViewState.Loaded, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        FlippableStatsChip(
            modifier = Modifier.weight(1f),
            gradient = MaterialTheme.gradients.shifter,
            iconId = MaterialTheme.assets.icons.longestStreak,
            frontContent = {
                ChipContent(
                    cardHeading = "${detailsPageData.longestStreakInProject.days} Days",
                    cardSubHeading = "Longest Streak",
                    gradient = MaterialTheme.gradients.shifter,
                    statValueTextStyle = MaterialTheme.typography.headlineSmall,
                )
            },
            backContent = {
                StreakRangeDisplay(
                    detailsPageData.longestStreakInProject,
                    MaterialTheme.gradients.shifter,
                )
            },
        )

        FlippableStatsChip(
            modifier = Modifier.weight(1f),
            gradient = MaterialTheme.gradients.reef,
            iconId = MaterialTheme.assets.icons.streak,
            frontContent = {
                ChipContent(
                    cardHeading = "${detailsPageData.currentStreakInProject.days} Days",
                    cardSubHeading = "Current Streak",
                    gradient = MaterialTheme.gradients.reef,
                    statValueTextStyle = MaterialTheme.typography.headlineSmall,
                )
            },
            backContent = { StreakRangeDisplay(detailsPageData.currentStreakInProject, MaterialTheme.gradients.reef) },
        )
    }
}

@Composable
private fun StreakRangeDisplay(streak: Streak, gradient: Gradient) {
    Text(
        text = streak.formattedPrintRange(),
        color = gradient.onStartColor,
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 18.sp
        ),
    )
    Text(
        text = "Streak Range",
        color = gradient.onStartColor,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
    )
}
