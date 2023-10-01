package com.jacob.wakatimeapp.details.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.mappers.fromDto
import com.jacob.wakatimeapp.core.models.Branch
import com.jacob.wakatimeapp.core.models.Machine
import com.jacob.wakatimeapp.core.models.ProjectStats
import com.jacob.wakatimeapp.core.models.StatsRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO.Data
import kotlinx.datetime.toLocalDate

fun DetailedProjectStatsDTO.toModel(name: String): ProjectStats {
    val dailyStats = data.associate {
        it.range.date.toLocalDate() to Time.fromDecimal(it.grandTotal.decimal.toFloat())
    }

    val editors = data.flatMap(Data::editors)
        .let(List<EditorDTO>::fromDto)

    val languages = data.flatMap(Data::languages)
        .let(List<LanguageDTO>::fromDto)

    val operatingSystems = data.flatMap(Data::operatingSystems)
        .let(List<OperatingSystemDTO>::fromDto)

    val branches = data.flatMap(Data::branches)
        .map { branch ->
            Branch(
                name = branch.name,
                time = Time.fromDecimal(branch.totalSeconds.toFloat()),
            )
        }

    val machines = data.flatMap(Data::machines)
        .map { machine ->
            Machine(
                name = machine.name,
                time = Time.fromDecimal(machine.totalSeconds.toFloat()),
            )
        }

    return ProjectStats(
        name = name,
        totalTime = Time.fromDecimal(cumulativeTotal.decimal.toFloat()),
        dailyProjectStats = dailyStats,
        range = StatsRange(startDate = start, endDate = end),
        languages = languages,
        operatingSystems = operatingSystems,
        editors = editors,
        branches = branches,
        machines = machines,
    )
}
