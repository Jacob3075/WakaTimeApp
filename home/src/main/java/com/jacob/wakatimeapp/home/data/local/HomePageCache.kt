package com.jacob.wakatimeapp.home.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.jacob.wakatimeapp.core.models.Error.DatabaseError
import com.jacob.wakatimeapp.home.domain.models.HomePageUiData
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class HomePageCache @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {

    suspend fun getLastRequestTime(): Instant = dataStore.data.map {
        val value = it[KEY_LAST_REQUEST_TIME]
        value?.let(Instant::fromEpochMilliseconds) ?: Instant.DISTANT_PAST
    }
        .catch { Instant.DISTANT_PAST }
        .first()

    suspend fun updateLastRequestTime(time: Instant = Clock.System.now()) {
        dataStore.edit {
            it[KEY_LAST_REQUEST_TIME] = time.toEpochMilliseconds()
        }
    }

    fun getCachedData() = dataStore.data.map {
        val emptyCacheError: Either<DatabaseError, Nothing> = DatabaseError.EmptyCache("")
            .left()
        val stringUiData = it[KEY_CACHED_HOME_PAGE_UI_DATA] ?: return@map emptyCacheError
        json.decodeFromString<HomePageUiData>(stringUiData)
            .right()
    }
        .catch {
            emit(
                DatabaseError.UnknownError(it.message!!, it)
                    .left()
            )
        }

    suspend fun updateCache(homePageUiData: HomePageUiData) {
        dataStore.edit {
            it[KEY_CACHED_HOME_PAGE_UI_DATA] = json.encodeToString(homePageUiData)
        }
    }

    companion object {
        private val KEY_LAST_REQUEST_TIME = longPreferencesKey("KEY_LAST_REQUEST_TIME")
        private val KEY_CACHED_HOME_PAGE_UI_DATA =
            stringPreferencesKey("KEY_CACHE_HOME_PAGE_UI_DATA")
    }
}
