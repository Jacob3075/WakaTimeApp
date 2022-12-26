package com.jacob.wakatimeapp.details.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO.Data
import com.jacob.wakatimeapp.details.domain.models.ProjectStats
import kotlinx.datetime.toLocalDate

fun DetailedProjectStatsDTO.toModel(): ProjectStats {
    val dailyStats = data.associate {
        it.range.date.toLocalDate() to Time.fromDecimal(it.grandTotal.decimal.toFloat())
    }

    val editors = data.flatMap(Data::editors)
        .map(EditorDTO::name)

    val languages = data.flatMap(Data::languages)
        .map(LanguageDTO::name)

    val operatingSystems = data.flatMap(Data::operatingSystems)
        .map(OperatingSystemDTO::name)

    return ProjectStats(
        totalTime = Time.fromDecimal(cummulativeTotal.decimal.toFloat()),
        dailyProjectStats = dailyStats,
        range = StatsRange(startDate = start, endDate = end),
        languages = languages,
        operatingSystems = operatingSystems,
        editors = editors,
    )
}
