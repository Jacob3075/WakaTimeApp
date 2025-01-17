package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.IntOffset
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editor
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Language
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machine
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystem
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import com.jacob.wakatimeapp.core.models.secondarystats.SecondaryStats
import com.jacob.wakatimeapp.core.ui.WtaComponentPreviews
import com.jacob.wakatimeapp.core.ui.theme.WakaTimeAppTheme
import com.jacob.wakatimeapp.core.ui.theme.colors.Gradient
import com.jacob.wakatimeapp.core.ui.theme.gradients
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

@Composable
fun SecondaryStatsCards(uiData: DetailedProjectStatsUiData, modifier: Modifier = Modifier) {
    var currentStat by remember { mutableStateOf(SecondaryStatsAnimationDetails.NONE) }

    Column(
        modifier = modifier
            .aspectRatio(1.4f)
            .clipToBounds(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            SecondaryStatsChip(
                uiData.languages,
                SecondaryStatsAnimationDetails.LANGUAGE,
                currentStat,
                modifier = Modifier.weight(1f),
                expand = {
                    currentStat = secondaryStatsAnimationDetails(currentStat, SecondaryStatsAnimationDetails.LANGUAGE)
                },
            )
            SecondaryStatsChip(
                uiData.editors,
                SecondaryStatsAnimationDetails.EDITOR,
                currentStat,
                modifier = Modifier.weight(1f),
                expand = {
                    currentStat = secondaryStatsAnimationDetails(currentStat, SecondaryStatsAnimationDetails.EDITOR)
                },
            )
        }
        Row(modifier = modifier.weight(1f)) {
            SecondaryStatsChip(
                uiData.operatingSystems,
                SecondaryStatsAnimationDetails.OS,
                currentStat,
                modifier = Modifier.weight(1f),
                expand = {
                    currentStat = secondaryStatsAnimationDetails(currentStat, SecondaryStatsAnimationDetails.OS)
                },
            )
            SecondaryStatsChip(
                uiData.machines,
                SecondaryStatsAnimationDetails.MACHINE,
                currentStat,
                modifier = Modifier.weight(1f),
                expand = {
                    currentStat = secondaryStatsAnimationDetails(currentStat, SecondaryStatsAnimationDetails.MACHINE)
                },
            )
        }
    }
}

private fun secondaryStatsAnimationDetails(
    currentStat: SecondaryStatsAnimationDetails,
    secondaryStatsAnimationDetails: SecondaryStatsAnimationDetails,
) = if (currentStat == SecondaryStatsAnimationDetails.NONE) {
    secondaryStatsAnimationDetails
} else {
    SecondaryStatsAnimationDetails.NONE
}

@Composable
private fun SecondaryStatsChip(
    secondaryStats: SecondaryStats<*>,
    chipType: SecondaryStatsAnimationDetails,
    selectedChip: SecondaryStatsAnimationDetails,
    modifier: Modifier = Modifier,
    gradient: Gradient = MaterialTheme.gradients.purpink,
    expand: () -> Unit,
) {
    val finalDirectionToMove = remember(chipType, selectedChip) { chipType.directionToMoveWRT(selectedChip) }
    val xValue = finalDirectionToMove.first * 1000
    val yValue = -finalDirectionToMove.second * 1000 // Y-Axis is INVERTED!!!

    val offsetAsState by animateIntOffsetAsState(
        targetValue = IntOffset(xValue, yValue),
    )
    Text(
        "Most used ${chipType.statName}: ${secondaryStats.mostUsed?.name ?: "N/A"}",
        modifier = modifier
            .offset { offsetAsState }
            .clickable(onClick = expand),
    )
}

@WtaComponentPreviews
@Composable
private fun SecondaryStatsCardPreview() {
    WakaTimeAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SecondaryStatsCards(
                uiData = DetailedProjectStatsUiData(
                    totalTime = Time.ZERO,
                    averageTime = Time.ZERO,
                    dailyProjectStats = emptyMap<LocalDate, Time>().toImmutableMap(),
                    languages = Languages(
                        listOf(
                            Language("L1", Time.fromDecimal(3f)),
                            Language("L2", Time.fromDecimal(1f)),
                            Language("L3", Time.fromDecimal(4f)),
                            Language("L4", Time.fromDecimal(3f)),
                        ),
                    ),
                    editors = Editors(
                        listOf(
                            Editor("E1", Time.fromDecimal(3f)),
                            Editor("E2", Time.fromDecimal(1f)),
                            Editor("E3", Time.fromDecimal(4f)),
                            Editor("E4", Time.fromDecimal(3f)),
                        ),
                    ),
                    operatingSystems = OperatingSystems(
                        listOf(
                            OperatingSystem("O1", Time.fromDecimal(3f)),
                            OperatingSystem("O2", Time.fromDecimal(1f)),
                            OperatingSystem("O3", Time.fromDecimal(4f)),
                            OperatingSystem("O4", Time.fromDecimal(3f)),
                        ),
                    ),
                    machines = Machines(
                        listOf(
                            Machine("M1", Time.fromDecimal(3f)),
                            Machine("M2", Time.fromDecimal(1f)),
                            Machine("M3", Time.fromDecimal(4f)),
                            Machine("M4", Time.fromDecimal(3f)),
                        ),
                    ),
                ),
            )
        }
    }
}
