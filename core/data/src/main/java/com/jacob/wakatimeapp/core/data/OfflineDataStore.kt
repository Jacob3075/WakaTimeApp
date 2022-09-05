package com.jacob.wakatimeapp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jacob.wakatimeapp.core.models.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default
import net.openid.appauth.AuthState
import javax.inject.Inject

class OfflineDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    fun getUserDetails(): Flow<UserDetails> =
        dataStore.data.map { preferences ->
            preferences[KEY_USER_DETAILS]?.let(Default::decodeFromString)
                ?: error("SHOULD NOT HAPPEN") // TODO: BUT IT DID 😕⁉
        }

    suspend fun updateUserDetails(userDetails: UserDetails) {
        dataStore.edit {
            it[KEY_USER_DETAILS] = Json.encodeToString(userDetails)
        }
    }

    fun getAuthState(): Flow<AuthState> = dataStore.data.map {
        val authStateString = it[KEY_AUTH_STATE] ?: ""
        when {
            authStateString.isEmpty() -> AuthState()
            else -> authStateString.let(AuthState::jsonDeserialize)
        }
    }

    suspend fun updateAuthState(newAuthState: AuthState?) {
        dataStore.edit {
            it[KEY_AUTH_STATE] = newAuthState?.jsonSerializeString() ?: ""
        }
    }

    companion object {
        const val STORE_NAME = "userDetails"

        val KEY_USER_DETAILS = stringPreferencesKey("user_details")
        val KEY_AUTH_STATE = stringPreferencesKey("auth_state_string")
    }
}
