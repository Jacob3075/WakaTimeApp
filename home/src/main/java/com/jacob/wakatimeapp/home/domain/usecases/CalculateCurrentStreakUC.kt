package com.jacob.wakatimeapp.home.domain.usecases

import arrow.core.Either
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.home.data.local.HomePageCache
import com.jacob.wakatimeapp.home.domain.models.StreakRange
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@Singleton
class CalculateCurrentStreakUC @Inject constructor(
    dispatcher: CoroutineContext = Dispatchers.IO,
    private val homePageCache: HomePageCache,
) {

    operator fun invoke(): Flow<Either<Error, StreakRange>> {
        TODO()
    }
}
