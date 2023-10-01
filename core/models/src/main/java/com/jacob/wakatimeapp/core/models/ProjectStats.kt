package com.jacob.wakatimeapp.core.models

import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

data class ProjectStats(
    val name: String,
    val totalTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val range: StatsRange,
    val languages: Languages,
    val operatingSystems: OperatingSystems,
    val editors: Editors,
    val branches: List<Branch>,
    val machines: List<Machine>,
) {
    operator fun plus(other: ProjectStats) = ProjectStats(
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
        val ZERO = ProjectStats(
            name = "",
            totalTime = Time.ZERO,
            dailyProjectStats = mapOf(),
            range = StatsRange.ZERO,
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
