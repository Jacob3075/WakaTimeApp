package com.jacob.wakatimeapp.core.common.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors.NoConnection
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors.Timeout
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import timber.log.Timber

abstract class BaseNetworkData(private val authTokenProvider: AuthTokenProvider) {
    protected val token
        get() = runBlocking { authTokenProvider.getFreshToken().first() }

    protected suspend fun <T> makeSafeApiCall(
        apiCall: suspend () -> Response<T>,
        methodName: String,
    ): Either<Error, T> = try {
        apiCall().checkResponse()
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, methodName)
    }
}

private fun <T> Response<T>.checkResponse(): Either<Error, T> {
    return if (isSuccessful) body()!!.right() else NetworkErrors.create(
        errorBody()?.string() ?: "",
        code(),
    ).left()
}

private fun handleNetworkException(
    exception: Throwable,
    methodName: String,
): Either<Error, Nothing> {
    Timber.e("Error while getting data for $methodName, reason: ${exception.message}")
    exception.printStackTrace()
    return when (exception) {
        is UnknownHostException -> NoConnection("No internet connection").left()
        is SocketTimeoutException -> Timeout("Request timed out for: $methodName").left()
        else -> NetworkErrors.create(exception.message!!).left()
    }
}
