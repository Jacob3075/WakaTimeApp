package com.jacob.wakatimeapp.core.common.data.local

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.dao.ApplicationDao
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.Error
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.LocalDate

@Singleton
class WakaTimeAppDB @Inject constructor(
    private val applicationDao: ApplicationDao,
) {
    suspend fun getStatsForDay(date: LocalDate) = applicationDao.getStatsForDay(date)

    suspend fun getStatsForProject(name: String) = applicationDao.getStatsForProject(name)

    suspend fun getDateRangeInDb() = applicationDao.getDateRangeInDb()

    suspend fun insertExtractedData(extractedDataDTO: ExtractedDataDTO): Either<Error, Unit> = Either.catch {
        applicationDao.insertStatesForDay(extractedDataDTO)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert extracted data into db", it) }

    suspend fun insertExtractedData(detailedDailyStats: List<DetailedDailyStats>): Either<Error, Unit> = Either.catch {
        applicationDao.insertStatesForDay(detailedDailyStats)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert aggregate stats data into db", it) }
}
