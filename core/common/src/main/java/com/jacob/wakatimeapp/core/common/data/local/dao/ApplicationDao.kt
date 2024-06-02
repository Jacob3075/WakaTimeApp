package com.jacob.wakatimeapp.core.common.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.remote.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.remote.mappers.toEntity
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
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
            LEFT JOIN ProjectPerDay ON DayEntity.date = ProjectPerDay.day
            WHERE date BETWEEN :startDate AND :endDate
        """,
    )
    suspend fun getStatsForRange(startDate: LocalDate, endDate: LocalDate): Map<DayEntity, ProjectPerDay?>

    @Query("SELECT * FROM ProjectPerDay WHERE name = :name")
    suspend fun getStatsForProject(name: String): List<ProjectPerDay>

    @Query("SELECT * FROM ProjectPerDay WHERE day = :date")
    suspend fun getProjectForDay(date: LocalDate): List<ProjectPerDay>

    @Upsert
    suspend fun insertStatesForDay(dayEntity: DayEntity)

    @Upsert
    suspend fun upsertProjectStatsForDay(projectsPerDay: List<ProjectPerDay>): List<Long>

    @Transaction
    suspend fun insertStatesForDay(extractedDataDTO: ExtractedDataDTO) {
        extractedDataDTO.days.forEach {
            val dayEntity = it.toEntity()
            insertStatesForDay(dayEntity)
            it.projects
                .map { project -> project.toEntity(dayEntity.date) }
                .let { projectsPerDays -> upsertProjectStatsForDay(projectsPerDays) }
        }
    }

    @Transaction
    suspend fun updateDbWithNewData(detailedDailyStats: List<DetailedDailyStats>) {
        detailedDailyStats
            .forEach {
                val statsForDayToInsert = combineStatsForDay(it, getStatsForDay(it.date))
                insertStatesForDay(statsForDayToInsert)
                val projectStats = combineProjectStats(it, it.date, getProjectForDay(it.date))
                upsertProjectStatsForDay(projectStats)
            }
    }

    @Query("SELECT * FROM ProjectPerDay")
    suspend fun getAllProjects(): List<ProjectPerDay>

    @Query("SELECT * FROM ProjectPerDay WHERE name LIKE :projectName")
    suspend fun getDetailsForProject(projectName: String): List<ProjectPerDay>
}

private fun combineStatsForDay(detailedDailyStats: DetailedDailyStats, oldStatsForDay: DayEntity?): DayEntity {
    val newStatsForDay = detailedDailyStats.toEntity()

    return if (oldStatsForDay == null) {
        newStatsForDay
    } else {
        val combinedStatsForDay = DayEntity(
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
    dayId: LocalDate,
    projectPerDays: List<ProjectPerDay>,
): List<ProjectPerDay> {
    it.projects
        .map { project -> project.toEntity(dayId) }
        .let { newProjectsPerDays ->
            val projectsByName = projectPerDays.associateBy(ProjectPerDay::name)

            return newProjectsPerDays.map { newProjectPerDay ->
                val oldProjectPerDay = projectsByName[newProjectPerDay.name]
                newProjectPerDay.copy(
                    projectPerDayId = oldProjectPerDay?.projectPerDayId ?: 0,
                )
            }
        }
}
