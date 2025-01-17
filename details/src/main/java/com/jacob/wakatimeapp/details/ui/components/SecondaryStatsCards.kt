package com.jacob.wakatimeapp.details.ui.components

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate

/**
 * expandable cards
 * could replace tabs?
 * when clicking on one of the cards will expand that card and shrink the other
 * will expand in both X and Y, show pie chart after expanding
 *
 * before clicking/expanding
 *
 * 🟥 🟦
 *
 * 🟧 🟪
 *
 * after clicking/expanding
 *
 * 🟥🟥
 *
 * 🟥🟥
 */
@Composable
fun SecondaryStatsCards(uiData: DetailedProjectStatsUiData, modifier: Modifier = Modifier) {
    var currentStat by remember { mutableStateOf(SecondaryStatsTypes.NONE) }

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
                SecondaryStatsTypes.LANGUAGE,
                currentStat,
                modifier = Modifier.weight(1f),
            ) { currentStat = SecondaryStatsTypes.LANGUAGE }
            SecondaryStatsChip(
                uiData.editors,
                SecondaryStatsTypes.EDITOR,
                currentStat,
                modifier = Modifier.weight(1f),
            ) { currentStat = SecondaryStatsTypes.EDITOR }
        }
        Row(modifier = modifier.weight(1f)) {
            SecondaryStatsChip(
                uiData.operatingSystems,
                SecondaryStatsTypes.OS,
                currentStat,
                modifier = Modifier.weight(1f),
            ) { currentStat = SecondaryStatsTypes.OS }
            SecondaryStatsChip(
                uiData.machines,
                SecondaryStatsTypes.MACHINE,
                currentStat,
                modifier = Modifier.weight(1f),
            ) { currentStat = SecondaryStatsTypes.MACHINE }
        }
    }
}

@Composable
private fun SecondaryStatsChip(
    secondaryStats: SecondaryStats<*>,
    chipType: SecondaryStatsTypes,
    selectedChip: SecondaryStatsTypes,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
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
            .clickable(onClick = onClick),
    )
}

class AnimationDirections(val xAxis: Boolean, val yAxis: Boolean)

@Suppress("BooleanLiteralArgument")
enum class SecondaryStatsTypes(
    val statName: String,
    private val index: Int,
    private val pushDirection: AnimationDirections,
) {
    LANGUAGE("Language", 0, AnimationDirections(true, false)),
    EDITOR("Editor", 1, AnimationDirections(false, false)),
    OS("Operating System", 2, AnimationDirections(true, true)),
    MACHINE("Machine", 3, AnimationDirections(false, true)),
    NONE("None", -1, AnimationDirections(false, false)), ;

    @Suppress("BooleanLiteralArgument")
    private val directionMap = mapOf(
        Pair(0, AnimationDirections(false, false)),
        Pair(1, AnimationDirections(true, false)),
        Pair(2, AnimationDirections(false, true)),
        Pair(3, AnimationDirections(true, true)),
    )

    private fun getDirectionRelativeTo(other: SecondaryStatsTypes): AnimationDirections {
        if (other == NONE) return directionMap[0]!!
        if (this == other) return directionMap[0]!!

        return directionMap[this xor other]!!
    }

    // other.pushDirection defines if the push force is positive or negative along the axis
    // canMoveAlongAxes defines if the chip should move along that particular axis (eg: won't move along X if both chips are on the same row)
    // if it can move along an axis, checks if it should move positive or negative
    fun directionToMoveWRT(other: SecondaryStatsTypes): Pair<Int, Int> {
        val canMoveAlongAxes = getDirectionRelativeTo(other)

        val finalXAxisMovement = if (canMoveAlongAxes.xAxis) if (other.pushDirection.xAxis) 1 else -1 else 0
        val finalYAxisMovement = if (canMoveAlongAxes.yAxis) if (other.pushDirection.yAxis) 1 else -1 else 0
        return Pair(finalXAxisMovement, finalYAxisMovement)
    }

    private infix fun xor(other: SecondaryStatsTypes): Int {
        return this.index xor other.index
    }
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
