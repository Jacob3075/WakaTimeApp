package com.jacob.wakatimeapp.details.domain.models

import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.LocalDate

data class ProjectStats(
    val totalTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val range: StatsRange,
    val languages: List<String>,
    val operatingSystems: List<String>,
    val editors: List<String>,
) {
    operator fun plus(other: ProjectStats) = ProjectStats(
        totalTime = totalTime + other.totalTime,
        dailyProjectStats = dailyProjectStats + other.dailyProjectStats,
        range = range + other.range,
        languages = languages + other.languages,
        operatingSystems = operatingSystems + other.operatingSystems,
        editors = editors + other.editors,
    )

    companion object {
        val ZERO = ProjectStats(
            totalTime = Time.ZERO,
            dailyProjectStats = mapOf(),
            range = StatsRange(startDate = "", endDate = ""),
            languages = listOf(),
            operatingSystems = listOf(),
            editors = listOf(),
        )
    }
}
