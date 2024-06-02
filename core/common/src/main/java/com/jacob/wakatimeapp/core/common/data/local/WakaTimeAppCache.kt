package com.jacob.wakatimeapp.core.common.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.jacob.wakatimeapp.core.common.utils.InstantProvider
import com.jacob.wakatimeapp.core.common.utils.log
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

@Singleton
class WakaTimeAppCache @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val instantProvider: InstantProvider,
) {
    fun getLastRequestTime() = getLastRequestTime(KEY_LAST_REQUEST_TIME)

    suspend fun updateLastRequestTime(time: Instant = instantProvider.now()) = updateLastRequestTime(
        time,
        KEY_LAST_REQUEST_TIME,
    )

    fun getLastRequestTime(key: Preferences.Key<Long> = KEY_LAST_REQUEST_TIME) = dataStore.data
        .map {
            val value = it[key]
            value?.let(Instant::fromEpochMilliseconds) ?: Instant.DISTANT_PAST
        }
        .catch { Instant.DISTANT_PAST }
        .distinctUntilChanged()
        .log("getLastRequestTime(${key.name})")

    suspend fun updateLastRequestTime(time: Instant = instantProvider.now(), key: Preferences.Key<Long> = KEY_LAST_REQUEST_TIME) {
        dataStore.edit {
            it[key] = time.toEpochMilliseconds()
        }
    }

    companion object {
        private val KEY_LAST_REQUEST_TIME = longPreferencesKey("KEY_LAST_REQUEST_TIME")
    }
}
