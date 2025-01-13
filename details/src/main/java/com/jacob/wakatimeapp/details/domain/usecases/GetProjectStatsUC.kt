package com.jacob.wakatimeapp.details.domain.usecases

import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.core.common.data.remote.mappers.toAggregateProjectStatsForRange
import com.jacob.wakatimeapp.core.models.Time
import com.jacob.wakatimeapp.details.domain.models.DetailedProjectStatsUiData
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableMap

@Singleton
internal class GetProjectStatsUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
) {
    suspend operator fun invoke(projectName: String) = either {
        val aggregateProjectStatsForRange = wakaTimeAppDB.getDetailsForProject(projectName)
            .bind()
            .let(List<ProjectPerDay>::toAggregateProjectStatsForRange)

        DetailedProjectStatsUiData(
            totalTime = aggregateProjectStatsForRange.totalTime,
            averageTime = Time.ZERO,
            dailyProjectStats = aggregateProjectStatsForRange.dailyProjectStats.toImmutableMap(),
            languages = aggregateProjectStatsForRange.languages,
            operatingSystems = aggregateProjectStatsForRange.operatingSystems,
            editors = aggregateProjectStatsForRange.editors,
            machines = aggregateProjectStatsForRange.machines,
        )
    }
}
