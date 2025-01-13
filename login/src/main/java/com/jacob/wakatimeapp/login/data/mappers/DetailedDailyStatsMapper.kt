package com.jacob.wakatimeapp.login.data.mappers

import com.jacob.wakatimeapp.core.common.data.remote.mappers.toModel
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.login.data.dtos.GetStatsForRangeResDTO
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

internal fun GetStatsForRangeResDTO.toDetailedDailyStatsModel(
    projectStatInRanges: Map<LocalDate, List<DetailedProjectStatsForDay>>,
) = data.map { dailyStats ->
    val projects = projectStatInRanges.getOrDefault(
        dailyStats.range.date.toLocalDate(),
        emptyList(),
    )
    DetailedDailyStats(
        date = dailyStats.range.date.toLocalDate(),
        projects = projects.toImmutableList(),
        languages = dailyStats.languages.toModel(),
        editors = dailyStats.editors.toModel(),
        operatingSystems = dailyStats.operatingSystems.toModel(),
        machines = dailyStats.machines.toModel(),
        timeSpent = Time.fromTotalSeconds(dailyStats.grandTotal.totalSeconds),
    )
}
