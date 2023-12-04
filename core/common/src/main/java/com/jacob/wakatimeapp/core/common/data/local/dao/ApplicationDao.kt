package com.jacob.wakatimeapp.core.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.mappers.toEntity
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
            LEFT JOIN ProjectPerDay ON DayEntity.dayId = ProjectPerDay.dayIdFk
            WHERE date BETWEEN :startDate AND :endDate
        """,
    )
    suspend fun getStatsForRange(startDate: LocalDate, endDate: LocalDate): Map<DayEntity, List<ProjectPerDay>>

    @Query("SELECT * FROM ProjectPerDay WHERE name = :name")
    suspend fun getStatsForProject(name: String): List<ProjectPerDay>

    @Query("SELECT * FROM ProjectPerDay WHERE dayIdFk = :dayIdFk")
    suspend fun getProjectForDay(dayIdFk: Long): List<ProjectPerDay>

    @Insert
    suspend fun insertStatesForDay(dayEntity: DayEntity): Long

    @Update
    suspend fun updateStatesForDay(dayEntity: DayEntity)

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
                val dayId = if (statsForDayToInsert.id == null) {
                    insertStatesForDay(statsForDayToInsert.data)
                } else {
                    updateStatesForDay(statsForDayToInsert.data)
                    statsForDayToInsert.id
                }
                val projectStats = combineProjectStats(it, dayId, getProjectForDay(dayId))
                upsertProjectStatsForDay(projectStats)
            }
    }
}

private fun combineStatsForDay(detailedDailyStats: DetailedDailyStats, oldStatsForDay: DayEntity?): Something<DayEntity> {
    val newStatsForDay = detailedDailyStats.toEntity()

    return if (oldStatsForDay == null) {
        Something(newStatsForDay, null)
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
        Something(combinedStatsForDay, oldStatsForDay.dayId)
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
            val projectsByName = projectPerDays.associateBy(ProjectPerDay::name)

            return newProjectsPerDays.map { newProjectPerDay ->
                val oldProjectPerDay = projectsByName[newProjectPerDay.name]
                newProjectPerDay.copy(
                    projectPerDayId = oldProjectPerDay?.projectPerDayId ?: 0,
                )
            }
        }
}

private data class Something<T>(val data: T, val id: Long?)
