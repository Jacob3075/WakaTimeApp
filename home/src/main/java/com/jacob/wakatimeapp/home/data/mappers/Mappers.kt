package com.jacob.wakatimeapp.home.data.mappers

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.core.models.project.Project
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO.Data
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.toLocalDate

internal fun List<DayWithProjects>.toDailyStateAggregate() = DailyStatsAggregate(
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

internal fun GetDailyStatsResDTO.toModel() = DailyStats(
    timeSpent = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    projectsWorkedOn = data.first().projects.map(ProjectDTO::toModel).toImmutableList(),
    mostUsedLanguage = "",
    mostUsedEditor = "",
    mostUsedOs = "",
    date = data.first().range.date.toLocalDate(),
)

internal fun GetStatsForRangeResDTO.toModel() = DailyStatsAggregate(
    values = getDailyStatsFromDto(data),
)

private fun getDailyStatsFromDto(data: List<Data>) = data.map {
    DailyStats(
        timeSpent = Time.createFrom(
            digitalString = it.grandTotal.digital,
            decimal = it.grandTotal.decimal,
        ),
        mostUsedEditor = it.editors.maxByOrNull(EditorDTO::percent)?.name ?: "NA",
        mostUsedLanguage = it.languages.maxByOrNull(LanguageDTO::percent)?.name ?: "NA",
        mostUsedOs = it.operatingSystems.maxByOrNull(OperatingSystemDTO::percent)?.name ?: "NA",
        date = it.range.date.toLocalDate(),
        projectsWorkedOn = it.projects
            .filterNot(ProjectDTO::isUnknownProject)
            .map(ProjectDTO::toModel)
            .toImmutableList(),
    )
}

internal fun GetLast7DaysStatsResDTO.toModel() = WeeklyStats(
    totalTime = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    dailyStats = getDailyStatsFromDto(data),
    range = Range(startDate = start, endDate = end),
)

private fun getDailyStatsFromDto(data: List<GetLast7DaysStatsResDTO.Data>) = data.map {
    DailyStats(
        timeSpent = Time.createFrom(
            digitalString = it.grandTotal.digital,
            decimal = it.grandTotal.decimal,
        ),
        mostUsedEditor = it.editors.maxByOrNull(EditorDTO::percent)?.name ?: "NA",
        mostUsedLanguage = it.languages.maxByOrNull(LanguageDTO::percent)?.name ?: "NA",
        mostUsedOs = it.operatingSystems.maxByOrNull(OperatingSystemDTO::percent)?.name ?: "NA",
        date = it.range.date.toLocalDate(),
        projectsWorkedOn = it.projects
            .filterNot(ProjectDTO::isUnknownProject)
            .map(ProjectDTO::toModel)
            .toImmutableList(),
    )
}.toImmutableList()
