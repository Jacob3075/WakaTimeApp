package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Project
import com.jacob.wakatimeapp.home.data.local.entities.Last7DaysStatsEntity
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

fun Last7DaysStatsEntity.toModel() = Last7DaysStats(
    timeSpentToday = timeSpentToday,
    projectsWorkedOn = projectsWorkedOn.toImmutableList(),
    weeklyTimeSpent = weeklyTimeSpent.toImmutableMap(),
    mostUsedLanguage = mostUsedLanguage,
    mostUsedEditor = mostUsedEditor,
    mostUsedOs = mostUsedOs,
)

fun Last7DaysStats.toEntity() = Last7DaysStatsEntity(
    timeSpentToday = timeSpentToday,
    projectsWorkedOn = projectsWorkedOn.toList(),
    weeklyTimeSpent = weeklyTimeSpent.toMap(),
    mostUsedLanguage = mostUsedLanguage,
    mostUsedEditor = mostUsedEditor,
    mostUsedOs = mostUsedOs,
)

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

fun List<DayWithProjects>.toLast7RangeDaysStats(): Last7DaysStats {
    val mostUsedLanguage = flatMap {
        it.projectsForDay.map { projectPerDay -> projectPerDay.languages.mostUsed }
    }.filterNotNull()
        .maxByOrNull { it.time.totalSeconds }

    val mostUsedEditor = flatMap {
        it.projectsForDay.map { projectPerDay -> projectPerDay.editors.mostUsed }
    }.filterNotNull()
        .maxByOrNull { it.time.totalSeconds }

    val mostUsedOperatingSystem = flatMap {
        it.projectsForDay.map { projectPerDay -> projectPerDay.operatingSystems.mostUsed }
    }.filterNotNull()
        .maxByOrNull { it.time.totalSeconds }

    val todaysStats = maxByOrNull { it.day.date }
    val projectWorkedOnToday = (todaysStats?.projectsForDay ?: emptyList())
        .filter { it.grandTotal != Time.ZERO }
        .toModel()
    return Last7DaysStats(
        timeSpentToday = todaysStats?.day?.grandTotal ?: Time.ZERO,
        projectsWorkedOn = projectWorkedOnToday,
        weeklyTimeSpent = map(DayWithProjects::day).associate { it.date to it.grandTotal }.toImmutableMap(),
        mostUsedLanguage = mostUsedLanguage?.name ?: "NA",
        mostUsedEditor = mostUsedEditor?.name ?: "NA",
        mostUsedOs = mostUsedOperatingSystem?.name ?: "NA",
    )
}
