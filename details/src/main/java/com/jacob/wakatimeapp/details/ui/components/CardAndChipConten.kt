package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.ui.WtaPreviews
import com.jacob.wakatimeapp.core.ui.modifiers.removeFontPadding
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.cardContent
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.spacing
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import com.jacob.wakatimeapp.details.ui.DetailsPageViewState
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

@Composable
internal fun ColumnScope.ChipContent(
    cardSubHeading: String,
    cardHeading: String,
    gradient: Gradient,
    rotationX: Float = 0f,
    statValueTextStyle: TextStyle = MaterialTheme.typography.displayLarge,
) {
    Text(
        text = cardHeading,
        color = gradient.onStartColor,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .removeFontPadding(statValueTextStyle)
            .graphicsLayer {
                this.rotationX = rotationX
            },
    )
    Text(
        text = cardSubHeading,
        color = gradient.onStartColor,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
        modifier = Modifier.graphicsLayer {
            this.rotationX = rotationX
        },
    )
}

@Composable
internal fun BoxScope.CardContent(
    cardSubHeading: String,
    cardHeading: String,
    gradient: Gradient,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .padding(bottom = MaterialTheme.spacing.extraSmall),
    ) {
        Text(
            text = cardHeading,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.displayLarge,
            color = gradient.onEndColor,
            modifier = Modifier.removeFontPadding(MaterialTheme.typography.displayLarge),
        )
        Text(
            text = cardSubHeading,
            style = MaterialTheme.typography.cardContent,
            color = gradient.onStartColor,
        )
    }
}

@WtaPreviews
@Composable
private fun ProjectHistoryListPreview() {
    WakaTimeAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            QuickStatsCards(
                DetailsPageViewState.Loaded(
                    "",
                    uiData = DetailedProjectStatsUiData(
                        totalTime = Time.ZERO,
                        averageTime = Time.ZERO,
                        dailyProjectStats = mapOf(
                            LocalDate.fromEpochDays(1) to Time.fromDecimal(3f),
                            LocalDate.fromEpochDays(2) to Time.fromDecimal(1f),
                            LocalDate.fromEpochDays(3) to Time.fromDecimal(2f),
                            LocalDate.fromEpochDays(4) to Time.fromDecimal(2f),
                            LocalDate.fromEpochDays(5) to Time.fromDecimal(4f),
                            LocalDate.fromEpochDays(6) to Time.fromDecimal(3f),
                            LocalDate.fromEpochDays(7) to Time.fromDecimal(2f),
                            LocalDate.fromEpochDays(8) to Time.fromDecimal(3f),
                            LocalDate.fromEpochDays(9) to Time.fromDecimal(4f),
                            LocalDate.fromEpochDays(10) to Time.fromDecimal(1f),
                        ).toImmutableMap(),
                        languages = Languages.NONE,
                        operatingSystems = OperatingSystems.NONE,
                        editors = Editors.NONE,
                        machines = Machines.NONE,
                    ),
                    todaysDate = LocalDate.fromEpochDays(1),
                ),
            )
        }
    }
}
