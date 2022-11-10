package com.jacob.wakatimeapp.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.components.cards.OtherStatsCard
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.assets
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.core.ui.theme.sectionSubtitle
import com.jacob.wakatimeapp.core.ui.theme.sectionTitle
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.home.domain.models.StreakRange

@Composable
fun OtherDailyStatsSection(
    onClick: () -> Unit,
    mostUsedLanguage: String,
    mostUsedOs: String,
    mostUsedEditor: String,
    currentStreak: StreakRange,
    longestStreak: StreakRange = StreakRange.ZERO,
    modifier: Modifier = Modifier,
    numberOfDaysWorked: Int,
) = Column(
    modifier = modifier.fillMaxWidth()
) {
    val spacing = MaterialTheme.spacing

    SectionHeader()
    Spacer(modifier = Modifier.height(spacing.extraSmall))

    StreakStats(
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        numberOfDaysWorked = numberOfDaysWorked
    )

    Spacer(modifier = Modifier.height(spacing.sMedium))

    SecondaryStats(
        mostUsedLanguage = mostUsedLanguage,
        onClick = onClick,
        mostUsedOs = mostUsedOs,
        mostUsedEditor = mostUsedEditor,
    )
}

@Composable
private fun StreakStats(
    currentStreak: StreakRange,
    longestStreak: StreakRange,
    numberOfDaysWorked: Int,
) {
    val spacing = MaterialTheme.spacing
    val gradients = MaterialTheme.gradients

    Row(
        modifier = Modifier.height(Min),
        horizontalArrangement = Arrangement.spacedBy(spacing.small)
    ) {
        CurrentStreakCard(
            currentStreak = longestStreak,
            gradient = gradients.flare,
            cornerPercentage = 10,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            CurrentStreakCard(
                currentStreak = currentStreak,
                gradient = gradients.amin,
                cornerPercentage = 20
            )
            CurrentStreakCard(
                currentStreak = currentStreak,
                gradient = gradients.reef,
                cornerPercentage = 20
            )
        }
    }
}

@Composable
private fun SecondaryStats(
    mostUsedLanguage: String,
    onClick: () -> Unit,
    mostUsedOs: String,
    mostUsedEditor: String,
) = Column {
    val gradients = MaterialTheme.gradients
    val spacing = MaterialTheme.spacing
    val icons = MaterialTheme.assets.icons

    OtherStatsCard(
        statsType = "Most Language Used",
        language = mostUsedLanguage,
        gradient = gradients.quepal,
        iconId = icons.codeFile,
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        statsType = "Most OS Used",
        language = mostUsedOs,
        gradient = gradients.purpink,
        iconId = icons.laptop,
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(spacing.sMedium))
    OtherStatsCard(
        statsType = "Most Used Editor ",
        language = mostUsedEditor,
        gradient = gradients.neuromancer,
        iconId = icons.code,
        onClick = onClick
    )
}

@Composable
private fun SectionHeader() = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxWidth()
) {
    val typography = MaterialTheme.typography
    Text(text = "Other Daily Stats", style = typography.sectionTitle)
    Text(text = "Details", color = colorScheme.primary, style = typography.sectionSubtitle)
}

@WtaPreviews
@Composable
fun OtherDailyStatsSectionPreview() = WakaTimeAppTheme {
    Surface {
        OtherDailyStatsSection(
            onClick = {},
            mostUsedLanguage = "Kotlin",
            mostUsedOs = "Linux",
            mostUsedEditor = "Android Studio",
            currentStreak = StreakRange.ZERO,
            modifier = Modifier.padding(horizontal = 8.dp),
            numberOfDaysWorked = 0,
        )
    }
}
