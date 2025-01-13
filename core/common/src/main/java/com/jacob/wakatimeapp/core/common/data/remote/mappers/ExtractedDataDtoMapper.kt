package com.jacob.wakatimeapp.core.common.data.remote.mappers

import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.remote.dtos.ExtractedDataDTO.DayDTO
import com.jacob.wakatimeapp.core.common.data.remote.dtos.ExtractedDataDTO.DayDTO.ProjectDTO
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.Branch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

fun DayDTO.toEntity() = DayEntity(
    date = date.toLocalDate(),
    grandTotal = Time(grandTotal.hours, grandTotal.minutes, grandTotal.decimal.toFloat()),
    editors = editors.fromDto(),
    languages = languages.fromDto(),
    operatingSystems = operatingSystems.fromDto(),
    machines = machines.fromDto(),
)

fun ProjectDTO.toEntity(day: LocalDate): ProjectPerDay {
    val branches = branches.map { branch ->
        Branch(
            name = branch.name,
            time = Time.fromTotalSeconds(branch.totalSeconds),
        )
    }

    return ProjectPerDay(
        projectPerDayId = 0,
        day = day,
        name = name,
        grandTotal = Time(grandTotal.hours, grandTotal.minutes, grandTotal.decimal.toFloat()),
        editors = editors.fromDto(),
        languages = languages.fromDto(),
        operatingSystems = operatingSystems.fromDto(),
        branches = branches,
        machines = machines.fromDto(),
    )
}
