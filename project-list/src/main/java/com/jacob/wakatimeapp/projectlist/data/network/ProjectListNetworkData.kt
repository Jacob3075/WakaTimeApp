package com.jacob.wakatimeapp.projectlist.data.network

import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Singleton
internal class ProjectListNetworkData @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val projectListAPI: ProjectListAPI,
) {
    private val token
        get() = runBlocking { authTokenProvider.getFreshToken().first() }
}
