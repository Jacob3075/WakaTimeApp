package com.jacob.wakatimeapp.login.data.mappers

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.mappers.fromDto
import com.jacob.wakatimeapp.core.common.data.mappers.toBranch
import com.jacob.wakatimeapp.core.common.data.mappers.toMachine
import com.jacob.wakatimeapp.core.common.data.mappers.toModel
import com.jacob.wakatimeapp.core.models.AggregateProjectStatsForRange
import com.jacob.wakatimeapp.core.models.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.login.data.dtos.DetailedProjectStatsDTO
import com.jacob.wakatimeapp.login.data.dtos.DetailedProjectStatsDTO.Data
import kotlinx.datetime.toLocalDate

fun DetailedProjectStatsDTO.toDetailedProjectStatsInRangeModel(name: String): AggregateProjectStatsForRange {
    val dailyStats = data.associate {
        it.range.date.toLocalDate() to Time.fromTotalSeconds(it.grandTotal.totalSeconds)
    }

    val editors = data.flatMap(Data::editors)
        .let(List<EditorDTO>::fromDto)

    val languages = data.flatMap(Data::languages)
        .let(List<LanguageDTO>::fromDto)

    val operatingSystems = data.flatMap(Data::operatingSystems)
        .let(List<OperatingSystemDTO>::fromDto)

    val branches = data.flatMap(Data::branches).toBranch()
    val machines = data.flatMap(Data::machines).toMachine()

    return AggregateProjectStatsForRange(
        name = name,
        totalTime = Time.fromTotalSeconds(cumulativeTotal.seconds),
        dailyProjectStats = dailyStats,
        range = Range(startDate = start, endDate = end),
        languages = languages,
        operatingSystems = operatingSystems,
        editors = editors,
        branches = branches,
        machines = machines,
    )
}

fun DetailedProjectStatsDTO.toDetailedProjectStatsForDayModel(name: String) = data.map {
    DetailedProjectStatsForDay(
        name = name,
        date = it.range.date.toLocalDate(),
        totalTime = Time.fromTotalSeconds(it.grandTotal.totalSeconds),
        languages = it.languages.toModel(),
        editors = it.editors.toModel(),
        operatingSystems = it.operatingSystems.toModel(),
        branches = it.branches.toBranch(),
        machines = it.machines.toMachine(),
    )
}
