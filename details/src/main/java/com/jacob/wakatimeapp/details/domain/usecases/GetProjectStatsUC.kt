package com.jacob.wakatimeapp.details.domain.usecases

import arrow.core.continuations.either
import com.jacob.wakatimeapp.core.common.toDate
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.data.ProjectDetailsNetworkData
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import com.jacob.wakatimeapp.details.domain.models.ProjectStats
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.datetime.DatePeriod
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

        val projectStats = generateSequence(totalProjectTime.startDate) { it + batchSize }
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

        DetailedProjectStatsUiData(
            totalProjectTime = totalProjectTime,
            averageTime = Time.ZERO,
            dailyProjectStats = projectStats.dailyProjectStats,
            languages = projectStats.languages,
            operatingSystems = projectStats.operatingSystems,
            editors = projectStats.editors,
        )
    }
}
