package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.continuations.either
import com.jacob.wakatimeapp.search.data.network.SearchProjectNetworkData
import com.jacob.wakatimeapp.search.domain.models.ProjectDetails
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.collections.immutable.toImmutableList

@Singleton
internal class GetAllProjectsUC @Inject constructor(
    private val searchProjectNetworkData: SearchProjectNetworkData,
) {
    suspend operator fun invoke() = either {
        var page = 1
        val projects = mutableListOf<ProjectDetails>()

        do {
            val projectListWithPageNumber = searchProjectNetworkData.getProjects(page).bind()

            projects.addAll(projectListWithPageNumber.projectList)

            if (projectListWithPageNumber.pageNumber == projectListWithPageNumber.totalPageCount) break

            ++page
        } while (true)

        projects.toImmutableList()
    }
}
