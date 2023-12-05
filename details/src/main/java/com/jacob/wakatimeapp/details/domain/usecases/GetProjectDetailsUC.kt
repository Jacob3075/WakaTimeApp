package com.jacob.wakatimeapp.details.domain.usecases

import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppDB
import com.jacob.wakatimeapp.details.data.ProjectDetailsNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GetProjectDetailsUC @Inject constructor(
    private val projectDetailsNetworkData: ProjectDetailsNetworkData,
    private val wakaTimeAppDB: WakaTimeAppDB,
) {
    suspend operator fun invoke(projectName: String) =
        wakaTimeAppDB.getDetailsForProject(projectName)
}
