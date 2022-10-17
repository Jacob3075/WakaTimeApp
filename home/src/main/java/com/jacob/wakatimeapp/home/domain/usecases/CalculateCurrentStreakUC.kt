package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.domain.models.Streaks
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CalculateCurrentStreakUC @Inject constructor() {

    operator fun invoke(): Flow<Either<Error, Streaks>> {
        TODO()
    }
}
