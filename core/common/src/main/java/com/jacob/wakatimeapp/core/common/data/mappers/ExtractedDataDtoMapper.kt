package com.jacob.wakatimeapp.core.common.data.mappers

import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO.DayDTO
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO.DayDTO.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Machine
import com.jacob.wakatimeapp.core.models.Time
import kotlinx.datetime.toLocalDate

fun DayDTO.toEntity() = DayEntity(
    dayId = 0,
    date = date.toLocalDate(),
    grandTotal = Time(grandTotal.hours, grandTotal.minutes, grandTotal.decimal.toFloat()),
    editors = editors.fromDto(),
    languages = languages.fromDto(),
    operatingSystems = operatingSystems.fromDto(),
    machines = this.machines.map { machine ->
        Machine(
            name = machine.name,
            time = Time.fromTotalSeconds(machine.totalSeconds),
        )
    },
)

fun ProjectDTO.toEntity(dayId: Long): ProjectPerDay {
    val branches = branches.map { branch ->
        com.jacob.wakatimeapp.core.models.Branch(
            name = branch.name,
            time = Time.fromTotalSeconds(branch.totalSeconds),
        )
    }
    val machines = machines.map { machine ->
        Machine(
            name = machine.name,
            time = Time.fromTotalSeconds(machine.totalSeconds),
        )
    }

    return ProjectPerDay(
        projectPerDayId = 0,
        dayIdFk = dayId,
        name = name,
        grandTotal = Time(grandTotal.hours, grandTotal.minutes, grandTotal.decimal.toFloat()),
        editors = editors.fromDto(),
        languages = languages.fromDto(),
        operatingSystems = operatingSystems.fromDto(),
        branches = branches,
        machines = machines,
    )
}
