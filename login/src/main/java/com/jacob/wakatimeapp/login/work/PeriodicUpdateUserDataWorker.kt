package com.jacob.wakatimeapp.login.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.jacob.wakatimeapp.core.common.auth.AuthTokenProvider
import com.jacob.wakatimeapp.login.domain.usecases.UpdateUserStatsInDbUC
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
internal class PeriodicUpdateUserDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authTokenProvider: AuthTokenProvider,
    private val updateUserStatsInDbUC: UpdateUserStatsInDbUC,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Timber.i("Worker started (PeriodicUpdateUserDataWorker)")

        if (!authTokenProvider.current.isAuthorized) return Result.failure()

        Timber.i("Updating user stats in database...")

        return when (updateUserStatsInDbUC()) {
            is Left -> Result.failure()
            is Right -> Result.success()
        }
    }

    companion object {
        const val WORK_NAME = "PeriodicUpdateUserDataWorker"
        const val WORK_INTERVAL_IN_DAYS = 7L
    }
}
