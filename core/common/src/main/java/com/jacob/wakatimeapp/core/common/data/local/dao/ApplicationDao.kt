package com.jacob.wakatimeapp.core.common.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.mappers.toEntity
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.core.models.Range
import kotlinx.datetime.LocalDate

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM DayEntity WHERE date = :date")
    suspend fun getStatsForDay(date: LocalDate): DayEntity?

    @Query("SELECT min(date) as startDate, max(date) as endDate FROM DayEntity")
    suspend fun getDateRangeInDb(): Range

    @Query(
        """
            SELECT * FROM DayEntity
            LEFT JOIN ProjectPerDay ON DayEntity.dayId = ProjectPerDay.dayIdFk
            WHERE date BETWEEN :startDate AND :endDate
        """,
    )
    suspend fun getStatsForRange(startDate: LocalDate, endDate: LocalDate): Map<DayEntity, List<ProjectPerDay>>

    @Query("SELECT * FROM ProjectPerDay WHERE name = :name")
    suspend fun getStatsForProject(name: String): List<ProjectPerDay>

    @Query("SELECT * FROM ProjectPerDay WHERE dayIdFk = :dayIdFk")
    suspend fun getProjectForDay(dayIdFk: Long): List<ProjectPerDay>

    @Upsert
    suspend fun insertStatesForDay(dayEntity: DayEntity): Long

    @Upsert
    suspend fun upsertProjectStatsForDay(projectsPerDay: List<ProjectPerDay>): List<Long>

    @Transaction
    suspend fun insertStatesForDay(extractedDataDTO: ExtractedDataDTO) {
        extractedDataDTO.days.forEach {
            val dayId = insertStatesForDay(it.toEntity())
            it.projects
                .map { project -> project.toEntity(dayId) }
                .let { projectsPerDays -> upsertProjectStatsForDay(projectsPerDays) }
        }
    }

    @Transaction
    suspend fun updateDbWithNewData(detailedDailyStats: List<DetailedDailyStats>) {
        detailedDailyStats
            .forEach {
                val statsForDayToInsert = combineStatsForDay(it, getStatsForDay(it.date))
                val dayId = insertStatesForDay(statsForDayToInsert)
                val projectStats = combineProjectStats(it, dayId, getProjectForDay(dayId))
                upsertProjectStatsForDay(projectStats)
            }
    }
}

private fun combineStatsForDay(detailedDailyStats: DetailedDailyStats, oldStatsForDay: DayEntity?): DayEntity {
    val newStatsForDay = detailedDailyStats.toEntity()

    return if (oldStatsForDay == null) {
        newStatsForDay
    } else {
        val combinedStatsForDay = DayEntity(
            dayId = oldStatsForDay.dayId,
            date = newStatsForDay.date,
            grandTotal = newStatsForDay.grandTotal,
            editors = newStatsForDay.editors,
            languages = newStatsForDay.languages,
            operatingSystems = newStatsForDay.operatingSystems,
            machines = newStatsForDay.machines,
        )
        combinedStatsForDay
    }
}

private fun combineProjectStats(
    it: DetailedDailyStats,
    dayId: Long,
    projectPerDays: List<ProjectPerDay>,
): List<ProjectPerDay> {
    it.projects
        .map { project -> project.toEntity(dayId) }
        .let { newProjectsPerDays ->
            if (projectPerDays.isEmpty()) {
                return newProjectsPerDays
            }

            val combinedProjectsPerDay = (projectPerDays + newProjectsPerDays)
                .groupBy(ProjectPerDay::name)
                .values
                .map { projectsPerDay ->
                    projectsPerDay.reduce { acc, projectPerDay ->
                        ProjectPerDay(
                            projectPerDayId = acc.projectPerDayId,
                            dayIdFk = acc.dayIdFk,
                            name = acc.name,
                            grandTotal = acc.grandTotal + projectPerDay.grandTotal,
                            editors = acc.editors + projectPerDay.editors,
                            languages = acc.languages + projectPerDay.languages,
                            operatingSystems = acc.operatingSystems + projectPerDay.operatingSystems,
                            branches = acc.branches + projectPerDay.branches,
                            machines = acc.machines + projectPerDay.machines,
                        )
                    }
                }

            return combinedProjectsPerDay
        }
}

fun DetailedProjectStatsForDay.toEntity(dayId: Long): ProjectPerDay {
    return ProjectPerDay(
        projectPerDayId = 0,
        dayIdFk = dayId,
        name = name,
        grandTotal = totalTime,
        editors = editors,
        languages = languages,
        operatingSystems = operatingSystems,
        branches = branches,
        machines = machines,
    )
}

fun DetailedDailyStats.toEntity() = DayEntity(
    dayId = 0,
    date = date,
    grandTotal = timeSpent,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    machines = emptyList(),
)
