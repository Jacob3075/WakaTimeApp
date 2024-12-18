package com.jacob.wakatimeapp.login.ui.loading

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import arrow.core.Either
import arrow.core.raise.either
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppCache
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserStatsInDbUC
import com.jacob.wakatimeapp.login.work.PeriodicUpdateUserDataWorker
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import java.util.concurrent.TimeUnit.DAYS
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Singleton
internal class InitialDataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authTokenProvider: AuthTokenProvider,
    private val wakaTimeAppCache: WakaTimeAppCache,
    private val instantProvider: InstantProvider,
    private val updateUserStatsInDbUC: UpdateUserStatsInDbUC,
) {

    suspend fun loadData(): Either<Error, Unit> = either {
        if (isUserLoggedIn().not()) {
            raise(Error.UnknownError("User not logged in"))
        }

        resetUpdateStatsWorker()
        checkDataInCache(1.minutes).bind()
    }

    private fun isUserLoggedIn(): Boolean = authTokenProvider.current.isAuthorized

    private fun resetUpdateStatsWorker() {
        val workManager = WorkManager.getInstance(getApplication(context).applicationContext)
        workManager.cancelAllWorkByTag(PeriodicUpdateUserDataWorker.WORK_NAME)

        val periodicWorkRequestBuilder = PeriodicWorkRequestBuilder<PeriodicUpdateUserDataWorker>(
            PeriodicUpdateUserDataWorker.WORK_INTERVAL_IN_DAYS,
            DAYS,
        ).addTag(PeriodicUpdateUserDataWorker.WORK_NAME)
            .setInitialDelay(
                PeriodicUpdateUserDataWorker.WORK_INTERVAL_IN_DAYS,
                DAYS,
            )
            .build()

        workManager.enqueue(periodicWorkRequestBuilder)
    }

    private suspend fun checkDataInCache(cacheValidityInMinutes: Duration = 15.minutes) = either {
        val lastRequestTime = wakaTimeAppCache.getLastRequestTime().firstOrNull() ?: Instant.DISTANT_PAST
        val isLastRequestYesterday = lastRequestTime.toLocalDateTime(instantProvider.timeZone).date.compareTo(instantProvider.date()) != 0

        if (isLastRequestYesterday) {
            Timber.d("first request of the day: $lastRequestTime")
            return@either updateUserStatsInDbUC().onRight { wakaTimeAppCache.updateLastRequestTime() }.bind()
        }

        val timeSinceLastRequest = instantProvider.now() - lastRequestTime
        if (timeSinceLastRequest >= cacheValidityInMinutes) {
            Timber.d("cache is stale: $lastRequestTime")
            return@either updateUserStatsInDbUC().onRight { wakaTimeAppCache.updateLastRequestTime() }.bind()
        }
        Timber.d("cache is fresh: $lastRequestTime")
    }
}
