package com.jacob.wakatimeapp.details.domain.usecases

import com.jacob.wakatimeapp.details.data.ProjectDetailsNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetProjectDetailsUC @Inject constructor(
    private val projectDetailsNetworkData: ProjectDetailsNetworkData,
) {
    suspend operator fun invoke(projectName: String) =
        projectDetailsNetworkData.getDetailsForProject(projectName)
}
