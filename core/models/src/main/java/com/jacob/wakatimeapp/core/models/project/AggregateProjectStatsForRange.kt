package com.jacob.wakatimeapp.core.models.project

import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

data class AggregateProjectStatsForRange(
    val name: String,
    val totalTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val range: Range,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val editors: Editors,
    val branches: List<Branch>,
    val machines: List<Machine>,
) {
    operator fun plus(other: AggregateProjectStatsForRange) = AggregateProjectStatsForRange(
        name = name,
        totalTime = totalTime + other.totalTime,
        dailyProjectStats = dailyProjectStats + other.dailyProjectStats,
        range = range + other.range,
        languages = languages + other.languages,
        operatingSystems = operatingSystems + other.operatingSystems,
        editors = editors + other.editors,
        branches = branches + other.branches,
        machines = machines + other.machines,
    )

    companion object {
        val ZERO = AggregateProjectStatsForRange(
            name = "",
            totalTime = Time.ZERO,
            dailyProjectStats = mapOf(),
            range = Range.ZERO,
            languages = Languages.NONE,
            operatingSystems = OperatingSystems.NONE,
            editors = Editors.NONE,
            branches = emptyList(),
            machines = emptyList(),
        )
    }
}

@Serializable
data class Branch(val name: String, val time: Time)

@Serializable
data class Machine(val name: String, val time: Time)
