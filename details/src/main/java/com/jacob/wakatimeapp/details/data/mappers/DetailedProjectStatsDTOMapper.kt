package com.jacob.wakatimeapp.details.data.mappers // ktlint-disable filename

import com.jacob.wakatimeapp.core.common.data.dtos.EditorDTO
import com.jacob.wakatimeapp.core.common.data.dtos.LanguageDTO
import com.jacob.wakatimeapp.core.common.data.dtos.OperatingSystemDTO
import com.jacob.wakatimeapp.core.common.data.mappers.fromDto
import com.jacob.wakatimeapp.core.models.Branch
import com.jacob.wakatimeapp.core.models.Machine
import com.jacob.wakatimeapp.core.models.ProjectStats
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO
import com.jacob.wakatimeapp.details.data.dtos.DetailedProjectStatsDTO.Data
import kotlinx.datetime.toLocalDate

fun DetailedProjectStatsDTO.toModel(name: String): ProjectStats {
    val dailyStats = data.associate {
        it.range.date.toLocalDate() to Time.fromTotalSeconds(it.grandTotal.totalSeconds)
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
                time = Time.fromTotalSeconds(branch.totalSeconds),
            )
        }

    val machines = data.flatMap(Data::machines)
        .map { machine ->
            Machine(
                name = machine.name,
                time = Time.fromTotalSeconds(machine.totalSeconds),
            )
        }

    return ProjectStats(
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
