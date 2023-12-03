package com.jacob.wakatimeapp.core.common.data.mappers

import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.Project
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun List<DayWithProjects>.toDailyStateAggregate() = DailyStatsAggregate(
    values = map {
        DailyStats(
            timeSpent = it.day.grandTotal,
            projectsWorkedOn = it.projectsForDay.map { projectPerDay ->
                Project(
                    time = projectPerDay.grandTotal,
                    name = projectPerDay.name,
                    percent = projectPerDay.grandTotal.totalSeconds / it.day.grandTotal.totalSeconds,
                )
            }.toImmutableList(),
            mostUsedLanguage = "",
            mostUsedEditor = "",
            mostUsedOs = "",
            date = it.day.date,
        )
    },
)

fun List<ProjectPerDay>.toModel(): ImmutableList<Project> {
    val totalSeconds = sumOf { it.grandTotal.totalSeconds }
    return map {
        Project(
            time = it.grandTotal,
            name = it.name,
            percent = it.grandTotal.totalSeconds / totalSeconds,
        )
    }.toImmutableList()
}
fun DetailedProjectStatsForDay.toEntity(dayId: Long): ProjectPerDay {
    return ProjectPerDay(
        projectPerDayId = 0,
        dayIdFk = dayId,
        name = name,
        grandTotal = totalTime,
        editors = editors,
        languages = languages,
        operatingSystems = operatingSystems,
        branches = branches,
        machines = machines,
    )
}

fun DetailedDailyStats.toEntity() = DayEntity(
    dayId = 0,
    date = date,
    grandTotal = timeSpent,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    machines = emptyList(),
)
