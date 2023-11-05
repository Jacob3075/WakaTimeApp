package com.jacob.wakatimeapp.details.domain.usecases

import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.AggregateProjectStatsForRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.ProjectDetailsNetworkData
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

@Singleton
internal class GetProjectStatsUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val projectStatsNetworkData: ProjectDetailsNetworkData,
    private val instantProvider: InstantProvider,
) {
    private val ioScope = CoroutineScope(dispatcher)

    suspend operator fun invoke(projectName: String) = either {
        val totalProjectTime = projectStatsNetworkData.getTotalTimeForProject(projectName).bind()

        val batchSize = DatePeriod(months = 6)
        val now = instantProvider.date()

        val aggregateProjectStatsForRange = generateSequence(totalProjectTime.startDate) { it + batchSize }
            .takeWhile { it < now }
            .plusElement(now)
            .zipWithNext()
            .map { (start, end) ->
                ioScope.async {
                    projectStatsNetworkData.getStatsForProject(
                        projectName = projectName,
                        startDate = start.toString(),
                        endDate = end.toString(),
                    )
                }
            }
            .toList()
            .awaitAll()
            .map { it.bind() }
            .fold(AggregateProjectStatsForRange.ZERO, AggregateProjectStatsForRange::plus)

        DetailedProjectStatsUiData(
            totalProjectTime = totalProjectTime,
            averageTime = Time.ZERO,
            dailyProjectStats = aggregateProjectStatsForRange.dailyProjectStats,
            languages = aggregateProjectStatsForRange.languages,
            operatingSystems = aggregateProjectStatsForRange.operatingSystems,
            editors = aggregateProjectStatsForRange.editors,
        )
    }
}
