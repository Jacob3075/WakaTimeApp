package com.jacob.wakatimeapp.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jacob.wakatimeapp.core.models.UserDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userDetails")

class OfflineDataStore @Inject constructor() {
    @ExperimentalCoroutinesApi
    fun getUserDetails(context: Context): Flow<UserDetails?> =
        context.dataStore.data.mapLatest { preferences ->
            preferences[KEY_USER_DETAILS]?.let(Default::decodeFromString)
        }

    suspend fun updateUserDetails(context: Context, userDetails: UserDetails) {
        context.dataStore.edit {
            it[KEY_USER_DETAILS] = Json.encodeToString(userDetails)
        }
    }

    companion object {
        val KEY_USER_DETAILS = stringPreferencesKey("user_details")
    }
}
