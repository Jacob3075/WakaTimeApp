package com.jacob.wakatimeapp.details.domain.usecases

import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.core.common.data.local.entities.ProjectPerDay
import com.jacob.wakatimeapp.details.data.toProjectDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetProjectDetailsUC @Inject constructor(
    private val wakaTimeAppDB: WakaTimeAppDB,
) {
    suspend operator fun invoke(projectName: String) =
        wakaTimeAppDB.getDetailsForProject(projectName).map(List<ProjectPerDay>::toProjectDetails)
}
