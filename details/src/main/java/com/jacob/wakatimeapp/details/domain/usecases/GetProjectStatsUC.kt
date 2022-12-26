package com.jacob.wakatimeapp.details.domain.usecases

import arrow.core.continuations.either
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.ProjectDetailsNetworkData
import com.jacob.wakatimeapp.details.domain.models.ProjectStats
import com.jacob.wakatimeapp.details.domain.models.TotalProjectTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Singleton
internal class GetProjectStatsUC @Inject constructor(
    private val projectStatsNetworkData: ProjectDetailsNetworkData,
    private val instantProvider: InstantProvider,
) {

    suspend operator fun invoke(projectName: String) = either {
        val totalProjectTime = projectStatsNetworkData.getTotalTimeForProject(projectName).bind()

        val batchSize = DatePeriod(months = 6)
        val now = instantProvider.now().toDate()

        generateSequence(totalProjectTime.startDate) { it + batchSize }
            .takeWhile { it < now }
            .plusElement(now)
            .zipWithNext()
            .toList()
            .map { (start, end) ->
                projectStatsNetworkData.getStatsForProject(
                    projectName = projectName,
                    startDate = start.toString(),
                    endDate = end.toString(),
                )
            }
            .map { it.bind() }
            .fold(ProjectStats.ZERO, ProjectStats::plus)
    }
}

data class Something(
    val totalProjectTime: TotalProjectTime,
    val averageTime: Time,
    val dailyProjectStats: Map<LocalDate, Time>,
    val languages: List<String>,
    val operatingSystems: List<String>,
    val editors: List<String>,
)
