package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.DayWithProjects
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.mappers.toLast7RangeDaysStats
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@Singleton
internal class GetLast7DaysStatsUC @Inject constructor(
    private val instantProvider: InstantProvider,
    private val wakaTimeAppDB: WakaTimeAppDB,
) {

    suspend operator fun invoke(): Either<Error, Last7DaysStats> {
        val today = instantProvider.date()
        val lastWeek7Days = today.minus(PREVIOUS_DAYS_IN_WEEK, DateTimeUnit.DAY)
        return wakaTimeAppDB.getStatsForRange(lastWeek7Days, today).map(List<DayWithProjects>::toLast7RangeDaysStats)
    }

    companion object {
        private const val PREVIOUS_DAYS_IN_WEEK = 6
    }
}
