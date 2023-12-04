package com.jacob.wakatimeapp.core.common.data.local

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.dtos.ExtractedDataDTO
import com.jacob.wakatimeapp.core.common.data.local.dao.ApplicationDao
import com.jacob.wakatimeapp.core.common.data.local.entities.DayEntity
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.mappers.toDailyStateAggregate
import com.jacob.wakatimeapp.core.models.DailyStatsAggregate
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

    suspend fun getDateRangeInDb(): Either<Error, Range> = Either.catch {
        applicationDao.getDateRangeInDb()
    }.mapLeft { Error.DatabaseError.UnknownError("could not get range from db", it) }

    suspend fun updateDbWithNewData(extractedDataDTO: ExtractedDataDTO): Either<Error, Unit> = Either.catch {
        applicationDao.insertStatesForDay(extractedDataDTO)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert extracted data into db", it) }

    suspend fun updateDbWithNewData(detailedDailyStats: List<DetailedDailyStats>): Either<Error, Unit> = Either.catch {
        applicationDao.updateDbWithNewData(detailedDailyStats)
    }.mapLeft { Error.DatabaseError.UnknownError("could not insert aggregate stats data into db", it) }

    suspend fun getAggregateStatsForRange(startDate: LocalDate, endDate: LocalDate): Either<Error, DailyStatsAggregate> = Either.catch {
        applicationDao.getStatsForRange(startDate, endDate).toDayWithProjects().toDailyStateAggregate()
    }.mapLeft { Error.DatabaseError.UnknownError("could not get stats for range ($startDate - $endDate)", it) }


    suspend fun getStatsForRange(startDate: LocalDate, endDate: LocalDate): Either<Error, List<DayWithProjects>> = Either.catch {
        applicationDao.getStatsForRange(startDate, endDate).toDayWithProjects()
    }.mapLeft { Error.DatabaseError.UnknownError("could not get stats for range ($startDate - $endDate)", it) }
}

private fun Map<DayEntity, List<ProjectPerDay>>.toDayWithProjects() = map { (dayEntity, projectsPerDay) ->
    DayWithProjects(
        day = dayEntity,
        projectsForDay = projectsPerDay,
    )
}
