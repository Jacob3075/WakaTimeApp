package com.jacob.wakatimeapp.core.common.data.mappers

import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.project.Project
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

fun List<ProjectPerDay>.toModel(): ImmutableList<Project> {
    val totalSeconds = sumOf { it.grandTotal.totalSeconds }
    return map {
        Project(
            time = it.grandTotal,
            name = it.name,
            percent = if (totalSeconds == 0.0) 0.0 else it.grandTotal.totalSeconds / totalSeconds,
        )
    }.toImmutableList()
}
fun DetailedProjectStatsForDay.toEntity(dayId: LocalDate) = ProjectPerDay(
    projectPerDayId = 0,
    day = dayId,
    name = name,
    grandTotal = totalTime,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    branches = branches,
    machines = machines,
)

fun DetailedDailyStats.toEntity() = DayEntity(
    date = date,
    grandTotal = timeSpent,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    machines = emptyList(),
)

fun Map<DayEntity, List<ProjectPerDay>>.toDayWithProjects() = map { (dayEntity, projectsPerDay) ->
    DayWithProjects(
        day = dayEntity,
        projectsForDay = projectsPerDay,
    )
}
