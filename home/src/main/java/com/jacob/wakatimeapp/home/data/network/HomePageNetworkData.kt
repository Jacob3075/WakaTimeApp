package com.jacob.wakatimeapp.home.data.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.models.DailyStats
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.core.models.Stats
import com.jacob.wakatimeapp.core.models.WeeklyStats
import com.jacob.wakatimeapp.home.data.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetStatsForRangeResDTO
import com.jacob.wakatimeapp.home.data.network.mappers.toModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import timber.log.Timber

@Singleton
internal class HomePageNetworkData @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val homePageAPI: HomePageAPI,
) {
    private val token
        get() = runBlocking { authTokenProvider.getFreshToken().first() }

    suspend fun getLast7DaysStats(): Either<Error, WeeklyStats> = try {
        homePageAPI.getLast7DaysStats("Bearer $token")
            .checkResponse()
            .map(GetLast7DaysStatsResDTO::toModel)
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, ::getLast7DaysStats.name)
    }

    suspend fun getStatsForToday(): Either<Error, DailyStats> = try {
        homePageAPI.getStatsForToday("Bearer $token")
            .checkResponse()
            .map(GetDailyStatsResDTO::toModel)
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, ::getStatsForToday.name)
    }

    suspend fun getData(): Either<Error, String> = try {
        homePageAPI.getData("Bearer $token")
            .checkResponse()
            .map(AllTimeDataDTO::toString)
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, ::getData.name)
    }

    suspend fun getStatsForRange(start: String, end: String): Either<Error, Stats> = try {
        homePageAPI.getStatsForRange("Bearer $token", start, end)
            .checkResponse()
            .map(GetStatsForRangeResDTO::toModel)
    } catch (exception: Exception) {
        Timber.e(exception)
        handleNetworkException(exception, ::getStatsForRange.name)
    }
}

private fun <T> Response<T>.checkResponse(): Either<Error, T> =
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
