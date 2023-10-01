package com.jacob.wakatimeapp.core.common.data.local

import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.dao.ApplicationDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.LocalDate

@Singleton
class WakaTimeDB @Inject constructor(
    private val applicationDao: ApplicationDao,
) {
    suspend fun getStatsForDay(date: LocalDate) = applicationDao.getStatsForDay(date)

    suspend fun getStatsForProject(name: String) = applicationDao.getStatsForProject(name)

    suspend fun insertExtractedData(extractedDataDTO: ExtractedDataDTO) =
        applicationDao.insertStatesForDay(extractedDataDTO)
}
