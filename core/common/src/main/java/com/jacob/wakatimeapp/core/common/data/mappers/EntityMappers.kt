package com.jacob.wakatimeapp.core.common.data.mappers

import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.core.models.project.AggregateProjectStatsForRange
import com.jacob.wakatimeapp.core.models.project.Branch
import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.project.Machine
import com.jacob.wakatimeapp.core.models.project.Project
import com.jacob.wakatimeapp.core.models.secondarystats.Editors
import com.jacob.wakatimeapp.core.models.secondarystats.Languages
import com.jacob.wakatimeapp.core.models.secondarystats.OperatingSystems
import kotlin.collections.Map.Entry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate

fun List<ProjectPerDay>.toModel(): ImmutableList<Project> {
    val totalSeconds = sumOf { it.grandTotal.totalSeconds }
    return map {
        Project(
            time = it.grandTotal,
            name = it.name,
            percent = if (totalSeconds == 0.0) 0.0 else it.grandTotal.totalSeconds / totalSeconds,
        )
    }.toImmutableList()
}

fun DetailedProjectStatsForDay.toEntity(dayId: LocalDate) = ProjectPerDay(
    projectPerDayId = 0,
    day = dayId,
    name = name,
    grandTotal = totalTime,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    branches = branches,
    machines = machines,
)

fun DetailedDailyStats.toEntity() = DayEntity(
    date = date,
    grandTotal = timeSpent,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    machines = emptyList(),
)

fun Map<DayEntity, ProjectPerDay?>.toDayWithProjects() = entries.groupBy {
    it.key.date
}.values.map {
    val dayEntity = (it.firstOrNull() ?: return@map null).key
    val projects = it.mapNotNull(Entry<DayEntity, ProjectPerDay?>::value)
    DayWithProjects(
        dayEntity,
        projects,
    )
}.filterNotNull()

fun List<ProjectPerDay>.toAggregateProjectStatsForRange(): AggregateProjectStatsForRange {
    val projectName = firstOrNull()?.name ?: return AggregateProjectStatsForRange.ZERO

    val startDate = minBy(ProjectPerDay::day).day
    val endDate = maxBy(ProjectPerDay::day).day
    val totalTime = map(ProjectPerDay::grandTotal).fold(Time.ZERO, Time::plus)

    val languages = flatMap { it.languages.values }.let(::Languages)
    val editors = flatMap { it.editors.values }.let(::Editors)
    val operatingSystems = flatMap { it.operatingSystems.values }.let(::OperatingSystems)

    val branches = flatMap(ProjectPerDay::branches).groupBy(Branch::name)
        .values
        .map {
            it.reduce { acc, branch -> acc.copy(time = acc.time + branch.time) }
        }

    val machines = flatMap(ProjectPerDay::machines).groupBy(Machine::name)
        .values
        .map {
            it.reduce { acc, branch -> acc.copy(time = acc.time + branch.time) }
        }

    val dailyProjectStats = groupBy(ProjectPerDay::day).mapValues { (_, projects) ->
        projects.map(ProjectPerDay::grandTotal).fold(Time.ZERO, Time::plus)
    }

    return AggregateProjectStatsForRange(
        name = projectName,
        totalTime = totalTime,
        dailyProjectStats = dailyProjectStats,
        range = Range(startDate = startDate, endDate = endDate),
        languages = languages,
        operatingSystems = operatingSystems,
        editors = editors,
        branches = branches,
        machines = machines,
    )
}
