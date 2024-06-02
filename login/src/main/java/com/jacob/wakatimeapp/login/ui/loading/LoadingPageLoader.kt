package com.jacob.wakatimeapp.login.ui.loading

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserStatsInDbUC
import com.jacob.wakatimeapp.login.work.PeriodicUpdateUserDataWorker
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit.DAYS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadingPageLoader @Inject constructor(
    private val authTokenProvider: AuthTokenProvider,
    @ApplicationContext private val context: Context,
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
        updateUserStatsInDbUC()

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
}
