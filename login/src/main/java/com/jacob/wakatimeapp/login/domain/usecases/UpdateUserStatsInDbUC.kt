package com.jacob.wakatimeapp.login.domain.usecases

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.data.dtos.ProjectDTO
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Range
import com.jacob.wakatimeapp.core.models.project.DetailedProjectStatsForDay
import com.jacob.wakatimeapp.login.data.LoginPageNetworkData
import com.jacob.wakatimeapp.login.data.mappers.toDetailedDailyStatsModel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus

@Singleton
internal class UpdateUserStatsInDbUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
    private val loginPaeNetworkData: LoginPageNetworkData,
    private val instantProvider: InstantProvider,
) {
    // TODO: SET AND CHECK LAST UPDATE TIME SIMILAR TO HOW HOME PAGE CACHE WORKS
    suspend operator fun invoke(): Either<Error, Unit> = either {
        val rangeInDb = wakaTimeAppDB.getDateRangeInDb().bind()
        val newRange = Range(rangeInDb.endDate, instantProvider.date())

        if (newRange.startDate < newRange.endDate.minus(DatePeriod(days = 14))) {
            return Error.DomainError.DataRangeTooLarge(
                "cannot get data for more than 14 days, create a new extract and import to get older data",
            ).left()
        }

        val statsForRangeDto = loginPaeNetworkData.getStatsForRange(
            start = newRange.startDate,
            end = newRange.endDate,
        ).bind()

        val projectNames = statsForRangeDto.data.flatMap { it.projects.map(ProjectDTO::name) }

        val projectStats = projectNames
            .map {
                loginPaeNetworkData.getStatsForProject(
                    projectName = it,
                    startDate = newRange.startDate,
                    endDate = newRange.endDate,
                )
            }
            .flatMap { it.bind() }
            .groupBy(DetailedProjectStatsForDay::date)

        val detailedDailyStatsModel = statsForRangeDto.toDetailedDailyStatsModel(projectStats)

        wakaTimeAppDB.updateDbWithNewData(detailedDailyStatsModel).bind()
    }
}
