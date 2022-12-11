package com.jacob.wakatimeapp.home.data.network.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO.Data
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.toLocalDate

fun GetLast7DaysStatsResDTO.toModel() = WeeklyStats(
    totalTime = Time.createFrom(cumulativeTotal.digital, cumulativeTotal.decimal),
    dailyStats = getDailyStatsFromDto(data),
    range = StatsRange(
        startDate = start.takeWhile { it != 'T' }.toLocalDate(),
        endDate = end.takeWhile { it != 'T' }.toLocalDate(),
    ),
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
}.toImmutableList()
