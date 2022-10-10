package com.jacob.wakatimeapp.home.data.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.NetworkErrors
import com.jacob.wakatimeapp.home.data.network.dtos.AllTimeDataDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetDailyStatsResDTO
import com.jacob.wakatimeapp.home.data.network.dtos.GetLast7DaysStatsResDTO
import com.jacob.wakatimeapp.home.data.network.mappers.toModel
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import timber.log.Timber

@Singleton
class HomePageNetworkData @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    private val homePageAPI: HomePageAPI,
) {
    private val token: String
        get() = runBlocking {
            authTokenProvider.getFreshToken()
                .first()
        }

    suspend fun getLast7DaysStats() = Either.catchAndFlatten {
        homePageAPI.getLast7DaysStats("Bearer $token")
            .checkResponse()
            .map(GetLast7DaysStatsResDTO::toModel)
    }
        .mapLeft(::handleNetworkException)

    suspend fun getStatsForToday() = Either.catchAndFlatten {
        homePageAPI.getStatsForToday("Bearer $token")
            .checkResponse()
            .map(GetDailyStatsResDTO::toModel)
    }
        .mapLeft(::handleNetworkException)

    suspend fun getData() = Either.catchAndFlatten {
        homePageAPI.getData("Bearer $token")
            .checkResponse()
            .map(AllTimeDataDTO::toString)
    }
        .mapLeft(::handleNetworkException)
}

private fun <T> Response<T>.checkResponse(): Either<Error, T> = if (isSuccessful) {
    body()!!.right()
} else {
    NetworkErrors.create(message(), code())
        .left()
}

private fun handleNetworkException(exception: Throwable): Error {
    Timber.e(exception.toString())
    return when (exception) {
        is UnknownHostException -> NetworkErrors.NoConnection("No internet connection")
        else -> NetworkErrors.create(exception.message!!)
    }
}
