package com.jacob.wakatimeapp.login.ui.loading

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppCache
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserStatsInDbUC
import com.jacob.wakatimeapp.login.work.PeriodicUpdateUserDataWorker
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit.DAYS
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber

@Singleton
class SplashScreenLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authTokenProvider: AuthTokenProvider,
    private val wakaTimeAppCache: WakaTimeAppCache,
    private val instantProvider: InstantProvider,
) {
    @Inject
    internal lateinit var updateUserStatsInDbUC: UpdateUserStatsInDbUC

    /**
     * @return false if failed to load data, else returns true if data was loaded successfully
     */
    suspend fun loadData(): Boolean {
        if (isUserLoggedIn().not()) {
            return false
        }

        resetUpdateStatsWorker()
        checkDataInCache()

        return true
    }

    private fun isUserLoggedIn(): Boolean {
        return authTokenProvider.current.isAuthorized
    }

    private fun resetUpdateStatsWorker() {
        val workManager = WorkManager.getInstance(getApplication(context).applicationContext)
        workManager.cancelAllWorkByTag(PeriodicUpdateUserDataWorker.WORK_NAME)

        val periodicWorkRequestBuilder = PeriodicWorkRequestBuilder<PeriodicUpdateUserDataWorker>(
            PeriodicUpdateUserDataWorker.WORK_INTERVAL_IN_DAYS,
            DAYS,
        ).addTag(PeriodicUpdateUserDataWorker.WORK_NAME)
            .build()

        workManager.enqueue(periodicWorkRequestBuilder)
    }

    private suspend fun checkDataInCache(cacheValidityInMinutes: Duration = 15.minutes) {
        val lastRequestTime = wakaTimeAppCache.getLastRequestTime().firstOrNull() ?: Instant.DISTANT_PAST
        val isLastRequestYesterday = lastRequestTime.toLocalDateTime(instantProvider.timeZone).date.compareTo(instantProvider.date()) != 0
        if (isLastRequestYesterday) {
            Timber.d("first request of the day: $lastRequestTime")
            updateUserStatsInDbUC()
            wakaTimeAppCache.updateLastRequestTime()
            return
        }

        val timeSinceLastRequest = instantProvider.now() - lastRequestTime
        if (timeSinceLastRequest >= cacheValidityInMinutes) {
            Timber.d("cache is stale: $lastRequestTime")
            updateUserStatsInDbUC()
            wakaTimeAppCache.updateLastRequestTime()
        }
        Timber.d("cache is fresh: $lastRequestTime")
    }
}
