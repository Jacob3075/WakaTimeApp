package com.jacob.wakatimeapp.projectlist.data.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.projectlist.data.network.dto.ProjectListDTO
import com.jacob.wakatimeapp.projectlist.data.network.mappers.ProjectDetails
import com.jacob.wakatimeapp.projectlist.data.network.mappers.toModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import timber.log.Timber

@Singleton
internal class ProjectListNetworkData @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val projectListAPI: ProjectListAPI,
) {
    private val token
        get() = runBlocking { authTokenProvider.getFreshToken().first() }

    suspend fun getAllProjects(): Either<Error, List<ProjectDetails>> = try {
        projectListAPI.getAllProjects("Bearer $token", 0)
            .checkResponse()
            .map(ProjectListDTO::toModel)
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, ::getAllProjects.name)
    }
}

private fun <T> Response<T>.checkResponse(): Either<com.jacob.wakatimeapp.core.models.Error, T> =
    if (isSuccessful) body()!!.right() else NetworkErrors.create(message(), code()).left()

private fun handleNetworkException(
    exception: Throwable,
    methodName: String,
): Either<Error, Nothing> {
    Timber.e("Error while getting data for $methodName, reason: ${exception.message}")
    exception.printStackTrace()
    return when (exception) {
        is UnknownHostException -> NetworkErrors.NoConnection("No internet connection").left()
        is SocketTimeoutException -> NetworkErrors.Timeout("Request timed out for: $methodName")
            .left()

        else -> NetworkErrors.create(exception.message!!).left()
    }
}
