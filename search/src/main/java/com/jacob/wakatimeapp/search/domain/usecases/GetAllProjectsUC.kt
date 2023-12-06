package com.jacob.wakatimeapp.search.domain.usecases

import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.search.data.toProjectDetails
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class GetAllProjectsUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
) {
    suspend operator fun invoke() = wakaTimeAppDB.getAllProjects()
        .map {
            it.toProjectDetails()
                .sortedBy { projectDetails -> projectDetails.range.endDate }
                .asReversed()
                .toImmutableList()
        }
}
