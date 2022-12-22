package com.jacob.wakatimeapp.details.data

import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.BaseNetworkData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProjectDetailsNetworkData @Inject constructor(
    authTokenProvider: AuthTokenProvider,
    private val projectDetailsPageAPI: ProjectDetailsPageAPI,
) : BaseNetworkData(authTokenProvider)
