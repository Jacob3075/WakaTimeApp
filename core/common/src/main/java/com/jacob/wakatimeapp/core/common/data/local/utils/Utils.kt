package com.jacob.wakatimeapp.core.common.data.local.utils

import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.Machines
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlinx.datetime.LocalDate

internal fun List<ProjectPerDay>.fillMissingDaysWithZeroValues(): List<ProjectPerDay> {
    val startDay = minOf(ProjectPerDay::day).toEpochDays()
    val endDate = maxOf(ProjectPerDay::day).toEpochDays()
    val dayToStats = associateBy { it.day.toEpochDays() }.toMutableMap()

    generateSequence(startDay) { it + 1 }
        .takeWhile { it <= endDate }
        .filter { day -> dayToStats[day] == null }
        .forEach { day ->
            dayToStats[day] = ProjectPerDay(
                projectPerDayId = 0,
                day = LocalDate.fromEpochDays(day),
                name = "",
                grandTotal = Time(0, 0, 0.0f),
                editors = Editors.NONE,
                languages = Languages.NONE,
                operatingSystems = OperatingSystems.NONE,
                branches = emptyList(),
                machines = Machines.NONE,
            )
        }

    return dayToStats.values.toList().sortedBy(ProjectPerDay::day)
}

internal fun List<DayWithProjects>.fillMissingDaysWithZeroValues(startDate: LocalDate, endDate: LocalDate): List<DayWithProjects> {
    val dayToStats = associateBy { it.day.date.toEpochDays() }.toMutableMap()
    val startDateEpoch = startDate.toEpochDays()
    val endDateEpoch = endDate.toEpochDays()

    generateSequence(startDateEpoch) { it + 1 }
        .takeWhile { it <= endDateEpoch }
        .filter { day -> dayToStats[day] == null }
        .forEach { day ->
            dayToStats[day] = DayWithProjects(
                day = DayEntity(
                    date = startDate,
                    grandTotal = Time.ZERO,
                    editors = Editors.NONE,
                    languages = Languages.NONE,
                    operatingSystems = OperatingSystems.NONE,
                    machines = Machines.NONE,
                ),
                projectsForDay = emptyList(),
            )
        }

    return dayToStats.values.toList().sortedBy { it.day.date }
}
