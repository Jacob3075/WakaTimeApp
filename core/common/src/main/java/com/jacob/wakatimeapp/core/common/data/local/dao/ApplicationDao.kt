package com.jacob.wakatimeapp.core.common.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.mappers.toEntity
import kotlinx.datetime.LocalDate

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM DayEntity WHERE date = :date")
    suspend fun getStatsForDay(date: LocalDate): List<DayEntity>

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
}
