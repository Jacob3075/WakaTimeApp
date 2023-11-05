package com.jacob.wakatimeapp.core.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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
    suspend fun getStatsForDay(date: LocalDate): List<DayEntity>

    @Query("SELECT min(date) as startDate, max(date) as endDate FROM DayEntity")
    suspend fun getDateRangeInDb(): Range?

    @Query("SELECT * FROM ProjectPerDay WHERE name = :name")
    suspend fun getStatsForProject(name: String): List<ProjectPerDay>

    @Insert
    suspend fun insertStatesForDay(dayEntity: DayEntity): Long

    @Insert
    suspend fun insertProjectStatsForDay(projectsPerDay: List<ProjectPerDay>): List<Long>

    @Transaction
    suspend fun insertStatesForDay(extractedDataDTO: ExtractedDataDTO) {
        extractedDataDTO.days.forEach {
            val dayId = insertStatesForDay(it.toEntity())
            it.projects
                .map { project -> project.toEntity(dayId) }
                .let { projectsPerDays -> insertProjectStatsForDay(projectsPerDays) }
        }
    }

    @Transaction
    suspend fun insertStatesForDay(detailedDailyStats: List<DetailedDailyStats>) {
        detailedDailyStats.forEach {
            val dayId = insertStatesForDay(it.toEntity())
            it.projects
                .map { project -> project.toEntity(dayId) }
                .let { projectsPerDays -> insertProjectStatsForDay(projectsPerDays) }
        }
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

private fun DetailedDailyStats.toEntity() = DayEntity(
    dayId = 0,
    date = date,
    grandTotal = timeSpent,
    editors = editors,
    languages = languages,
    operatingSystems = operatingSystems,
    machines = emptyList(),
)
