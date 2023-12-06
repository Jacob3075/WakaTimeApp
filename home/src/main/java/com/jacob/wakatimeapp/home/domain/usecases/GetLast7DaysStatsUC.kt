package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.home.data.mappers.toLast7RangeDaysStats
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

@Singleton
internal class GetLast7DaysStatsUC @Inject constructor(
    private val instantProvider: InstantProvider,
    private val wakaTimeAppDB: WakaTimeAppDB,
) {

    suspend operator fun invoke() = either {
        val today = instantProvider.date()
        val lastWeek7Days = today.minus(DAYS_IN_WEEK, DateTimeUnit.DAY)
        wakaTimeAppDB.getStatsForRange(lastWeek7Days, today).bind().toLast7RangeDaysStats()
    }

    companion object {
        private const val DAYS_IN_WEEK = 7
    }
}
