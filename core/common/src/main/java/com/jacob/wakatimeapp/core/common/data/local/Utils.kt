package com.jacob.wakatimeapp.core.common.data.local

import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
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
                machines = emptyList(),
            )
        }

    return dayToStats.values.toList().sortedBy(ProjectPerDay::day)
}
