package com.jacob.wakatimeapp.search.domain.usecases

import arrow.core.Either
import arrow.core.continuations.either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.search.data.network.SearchProjectNetworkData
import com.jacob.wakatimeapp.search.data.network.mappers.ProjectDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetAllProjectsUC @Inject constructor(
    private val searchProjectNetworkData: SearchProjectNetworkData,
) {
    suspend operator fun invoke(): Either<Error, List<ProjectDetails>> = either {
        var page = 1
        val projects = mutableListOf<ProjectDetails>()

        do {
            val projectListWithPageNumber = searchProjectNetworkData.getProjects(page).bind()

            projects.addAll(projectListWithPageNumber.projectList)

            if (projectListWithPageNumber.pageNumber == projectListWithPageNumber.totalPageCount) break

            ++page
        } while (true)

        projects
    }
}
