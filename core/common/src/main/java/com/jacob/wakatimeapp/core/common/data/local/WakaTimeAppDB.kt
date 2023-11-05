package com.jacob.wakatimeapp.core.common.data.local

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.dao.ApplicationDao
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.models.DetailedDailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Range
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.LocalDate

@Singleton
class WakaTimeAppDB @Inject constructor(
    private val applicationDao: ApplicationDao,
) {
    suspend fun getStatsForDay(date: LocalDate): Either<Error, List<DayEntity>> = Either.catch {
        applicationDao.getStatsForDay(date)
    }.mapLeft { Error.DatabaseError.UnknownError("could not get stats for day ($date)", it) }

    suspend fun getStatsForProject(name: String): Either<Error, List<ProjectPerDay>> = Either.catch {
        applicationDao.getStatsForProject(name)
    }.mapLeft { Error.DatabaseError.UnknownError("could not get stats for project ($name)", it) }

    suspend fun getDateRangeInDb(): Either<Error, Range> = Either.catch {
        applicationDao.getDateRangeInDb()
    }.mapLeft { Error.DatabaseError.UnknownError("could not get range from db", it) }

    suspend fun insertExtractedData(extractedDataDTO: ExtractedDataDTO): Either<Error, Unit> = Either.catch {
        applicationDao.insertStatesForDay(extractedDataDTO)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert extracted data into db", it) }

    suspend fun insertExtractedData(detailedDailyStats: List<DetailedDailyStats>): Either<Error, Unit> = Either.catch {
        applicationDao.insertStatesForDay(detailedDailyStats)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert aggregate stats data into db", it) }
}
