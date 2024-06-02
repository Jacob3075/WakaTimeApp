package com.jacob.wakatimeapp.home.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.common.data.local.WakaTimeAppCache
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.common.utils.log
import com.jacob.wakatimeapp.core.models.Error
import com.jacob.wakatimeapp.core.models.Error.DatabaseError
import com.jacob.wakatimeapp.home.data.local.entities.Last7DaysStatsEntity
import com.jacob.wakatimeapp.home.data.mappers.toEntity
import com.jacob.wakatimeapp.home.data.mappers.toModel
import com.jacob.wakatimeapp.home.domain.models.Last7DaysStats
import com.jacob.wakatimeapp.home.domain.models.Streak
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@Singleton
class HomePageCache @Inject constructor(
    private val wakaTimeAppCache: WakaTimeAppCache,
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val instantProvider: InstantProvider,
) {

    fun getLastRequestTime() = wakaTimeAppCache.getLastRequestTime(KEY_LAST_REQUEST_TIME)

    suspend fun updateLastRequestTime(time: Instant = instantProvider.now()) =
        wakaTimeAppCache.updateLastRequestTime(time, KEY_LAST_REQUEST_TIME)

    fun getLast7DaysStats() = dataStore.data.map {
        it[KEY_LAST_7_DAYS_STATS]?.let<String, Last7DaysStatsEntity>(json::decodeFromString)
            ?.toModel().right()
    }.catch<Either<Error, Last7DaysStats?>> {
        Timber.e(it)
        emit(DatabaseError.UnknownError(it.message!!, it).left())
    }.distinctUntilChanged()
        .log("getLast7DaysStats")

    suspend fun updateLast7DaysStats(homePageUiData: Last7DaysStats) {
        dataStore.edit {
            it[KEY_LAST_7_DAYS_STATS] = json.encodeToString(homePageUiData.toEntity())
        }
    }

    fun getCurrentStreak() = getStreak(KEY_CURRENT_STREAK).distinctUntilChanged()
        .log("currentStreak")

    suspend fun updateCurrentStreak(streak: Streak) {
        dataStore.edit {
            it[KEY_CURRENT_STREAK] = json.encodeToString(streak)
        }
    }

    fun getLongestStreak() = getStreak(KEY_LONGEST_STREAK).distinctUntilChanged()
        .log("longestStreak")

    suspend fun updateLongestStreak(streak: Streak) {
        dataStore.edit {
            it[KEY_LONGEST_STREAK] = json.encodeToString(streak)
        }
    }

    private fun getStreak(key: Preferences.Key<String>) =
        dataStore.data.map<Preferences, Either<Error, Streak>> {
            val streak = it[key]?.let<String, Streak>(json::decodeFromString)
                ?: Streak.ZERO
            streak.right()
        }.catch {
            Timber.e(it)
            emit(DatabaseError.UnknownError(it.message!!, it).left())
        }

    companion object {
        private val KEY_LAST_REQUEST_TIME = longPreferencesKey("KEY_LAST_REQUEST_TIME")
        private val KEY_LAST_7_DAYS_STATS = stringPreferencesKey("KEY_LAST_7_DAYS_STATS")
        private val KEY_CURRENT_STREAK = stringPreferencesKey("KEY_CURRENT_STREAK")
        private val KEY_LONGEST_STREAK = stringPreferencesKey("KEY_LONGEST_STREAK")
    }
}
